package achwie.shop.order.write;

import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.AuthService;
import achwie.shop.order.CatalogService;
import achwie.shop.order.ProductDetails;
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.domain.OrderStatus;
import achwie.shop.order.write.event.OrderConfirmed;
import achwie.shop.order.write.event.OrderPostedByCustomer;
import achwie.shop.order.write.eventhandler.EventVersions;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@RestController
@RequestMapping("/orders")
public class OrderWriteController {
  private static final ResponseEntity<String> RESPONSE_SUCCESS = new ResponseEntity<String>("OK", HttpStatus.OK);
  private static final ResponseEntity<String> RESPONSE_FAILED = new ResponseEntity<String>("Could not post order (maybe items were not in stock?)",
      HttpStatus.CONFLICT);
  private static final Logger LOG = LoggerFactory.getLogger(OrderWriteController.class);
  private final EventStore eventStore;
  private final IdGenerator idGenerator;
  private final AuthService authService;
  private final CatalogService catalogService;
  private final EventHandlerChain handlerChain;

  @Autowired
  public OrderWriteController(EventStore eventStore, IdGenerator idGenerator, AuthService authService, CatalogService catalogService,
      EventHandlerChain handlerChain) {
    this.eventStore = eventStore;
    this.idGenerator = idGenerator;
    this.authService = authService;
    this.catalogService = catalogService;
    this.handlerChain = handlerChain;
  }

  @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
  public ResponseEntity<String> placeOrder(@PathVariable String sessionId, @RequestBody Cart cart) {
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    if (sessionUserId == null)
      return new ResponseEntity<String>("Insufficient privileges for operation!", HttpStatus.UNAUTHORIZED);

    if (cart == null || cart.isEmpty())
      return new ResponseEntity<String>("ERROR: Can't send an empty order!.", HttpStatus.BAD_REQUEST);

    final boolean success = postOrder(sessionUserId, cart);

    return success ? RESPONSE_SUCCESS : RESPONSE_FAILED;
  }

  private boolean postOrder(String userId, Cart cart) {
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

    return saveAndWaitForOrderConfirmation(orderPosted);
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

  private boolean saveAndWaitForOrderConfirmation(OrderPostedByCustomer orderPosted) {
    final String orderId = orderPosted.getOrderId();

    final CountDownLatch waitForConfirmationLatch = new CountDownLatch(1);
    final WaitForOrderConfirmed waitForOrderConfirmedListener = new WaitForOrderConfirmed(orderId, waitForConfirmationLatch);
    final WaitForOrderFailed waitForOrderFailedListener = new WaitForOrderFailed(orderId, waitForConfirmationLatch);

    handlerChain.addEventHandler(waitForOrderConfirmedListener);
    handlerChain.addEventHandler(waitForOrderFailedListener);

    boolean success = false;
    try {
      eventStore.save(orderPosted.getAggregateId(), orderPosted);

      try {
        // Wait for async confirmationn/failure of order
        waitForConfirmationLatch.await(3, TimeUnit.SECONDS);

        final List<DomainEvent> orderHistory = eventStore.load(orderId);
        final MutableOrder confirmedOrder = new MutableOrder(orderHistory);
        final OrderStatus confirmedOrderStatus = confirmedOrder.getStatus();

        success = EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.PAYED, OrderStatus.SHIPPED).contains(confirmedOrderStatus);
      } catch (InterruptedException e) {
        // Count down latch timed out - order was not confirmed in time
        Thread.interrupted(); // Reset flag
      }
    } finally {
      handlerChain.removeEventHandler(waitForOrderFailedListener);
      handlerChain.removeEventHandler(waitForOrderConfirmedListener);
    }

    return success;
  }

  // ---------------------------------------------------------------------------
  private static abstract class WaitForOrderConfirmedOrFailed<T extends DomainEvent> implements EventHandler<T> {
    private final String orderId;
    private final CountDownLatch waitForConfirmationLatch;

    public WaitForOrderConfirmedOrFailed(String orderId, CountDownLatch waitForConfirmationLatch) {
      this.orderId = orderId;
      this.waitForConfirmationLatch = waitForConfirmationLatch;
    }

    @Override
    public void onEvent(T event) {
      if (event.getAggregateId().equals(orderId)) {
        waitForConfirmationLatch.countDown();
      }
    }
  }

  private static final class WaitForOrderConfirmed extends WaitForOrderConfirmedOrFailed<OrderConfirmed> {
    public WaitForOrderConfirmed(String orderId, CountDownLatch waitForConfirmationLatch) {
      super(orderId, waitForConfirmationLatch);
    }

    @Override
    public EventVersion getProcessableEventVersion() {
      return EventVersions.ORDER_CONFIRMED_1_0;
    }
  }

  private static final class WaitForOrderFailed extends WaitForOrderConfirmedOrFailed<OrderConfirmed> {

    public WaitForOrderFailed(String orderId, CountDownLatch waitForConfirmationLatch) {
      super(orderId, waitForConfirmationLatch);
    }

    @Override
    public EventVersion getProcessableEventVersion() {
      return EventVersions.ORDER_FAILED_TO_PUT_HOLD_ON_PRODUCTS_1_0;
    }
  }
}
