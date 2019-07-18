package achwie.shop.order.write;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import achwie.shop.eventstore.EventStore;
import achwie.shop.order.AuthService;
import achwie.shop.order.CatalogService;
import achwie.shop.order.ProductDetails;
import achwie.shop.order.message.OrderConfirmedEvent;
import achwie.shop.order.message.OrderUpdatedEvent;
import achwie.shop.order.message.PutProductsOnHoldMessage;
import achwie.shop.order.message.SubmitOrderMessage;
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.event.OrderPostedByCustomer;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Component
@KafkaListener(topics = "${kafka.topic.order}")
public class OrderWriteController {
  private static final Logger LOG = LoggerFactory.getLogger(OrderWriteController.class);
  private final EventStore eventStore;
  private final IdGenerator idGenerator;
  private final AuthService authService;
  private final CatalogService catalogService;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final String kafkaTopicOrder;

  @Autowired
  public OrderWriteController(EventStore eventStore, IdGenerator idGenerator, AuthService authService, CatalogService catalogService,
      @Value("${kafka.topic.order}") String kafkaTopicOrder, KafkaTemplate<String, Object> kafkaTemplate) {
    this.eventStore = eventStore;
    this.idGenerator = idGenerator;
    this.authService = authService;
    this.catalogService = catalogService;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTopicOrder = kafkaTopicOrder;
  }

  @KafkaHandler
  public void onSubmitOrder(SubmitOrderMessage submitOrderMessage) {
    final String sessionId = submitOrderMessage.getSessionId();
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    postOrder(sessionUserId, new Cart(submitOrderMessage.getItems()));
  }

  @KafkaHandler
  public void onOrderConfirmed(OrderConfirmedEvent orderConfirmedEvent) {
    final var orderId = orderConfirmedEvent.getOrderId();
    final var orderEvents = eventStore.load(orderId);
    final var order = new MutableOrder(orderEvents);

    final var orderConfirmed = order.confirmOrder();

    eventStore.save(orderConfirmed.getAggregateId(), orderConfirmed);

    kafkaTemplate.send(kafkaTopicOrder, new OrderUpdatedEvent(order.getUserId(), orderId));
  }

  @KafkaHandler(isDefault = true)
  public void onUnknown(Object message) {
    LOG.info("Received message of unknown type: " + message);
  }

  private void postOrder(String userId, Cart cart) {
    LOG.info("Received order with cart {}", cart);

    final int itemCount = cart.getItems().size();
    final ZonedDateTime orderTime = ZonedDateTime.now();
    final String[] productIds = new String[itemCount];
    final int[] quantities = new int[itemCount];

    for (int i = 0; i < itemCount; i++) {
      final CartItem item = cart.getItems().get(i);
      productIds[i] = item.getProductId();
      quantities[i] = item.getQuantity();
    }

    final ProductDetails[] allProductDetails = getProductDetails(productIds);

    final MutableOrder order = new MutableOrder();
    final String newOrderId = idGenerator.nextOrderId();
    final OrderPostedByCustomer orderPosted = order.postOrder(newOrderId, userId, orderTime, productIds, quantities, allProductDetails);

    eventStore.save(orderPosted.getAggregateId(), orderPosted);
    // TODO: Only save order if user has entered an address
    // TODO: Only save order if items are in stock
    kafkaTemplate.send(kafkaTopicOrder, new PutProductsOnHoldMessage(orderPosted.getOrderId(), productIds, quantities));
    // TODO: Send notification so third parties can react (e.g. flush cache)
    // kafkaTemplate.send(kafkaTopicOrder, new
    // OrderUpdatedEvent(orderPosted.getUserId(), orderPosted.getOrderId()));
  }

  private ProductDetails[] getProductDetails(String[] productIds) {
    // Not very smart but good enough for the example...
    ProductDetails[] allProductDetails = new ProductDetails[productIds.length];
    for (int i = 0; i < productIds.length; i++) {
      final ProductDetails productDetails = catalogService.fetchProductDetails(productIds[i]);
      allProductDetails[i] = productDetails;
      if (productDetails == null) {
        // TODO: What to do (throw exception? add "special" product details?)
        LOG.error("Could not get product details for product with ID {}", productIds[i]);
      }
    }

    return allProductDetails;
  }
}
