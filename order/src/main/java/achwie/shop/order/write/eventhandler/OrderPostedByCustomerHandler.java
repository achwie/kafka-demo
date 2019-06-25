package achwie.shop.order.write.eventhandler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.StockService;
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.event.FailedToPutHoldOnProducts;
import achwie.shop.order.write.event.OrderConfirmed;
import achwie.shop.order.write.event.OrderPostedByCustomer;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderPostedByCustomerHandler implements EventHandler<OrderPostedByCustomer> {
  private static final Logger LOG = LoggerFactory.getLogger(OrderPostedByCustomer.class);
  private final EventStore eventStore;
  private final StockService stockService;

  @Autowired
  public OrderPostedByCustomerHandler(EventHandlerChain handlerChain, EventStore eventStore, StockService stockService) {
    this.eventStore = eventStore;
    this.stockService = stockService;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderPostedByCustomer event) {
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    LOG.info("Received order under id {}", order.getId());

    // Place hold on products
    final List<String> productIdsNotInStock = stockService.putHoldOnAll(event.getProductIds(), event.getQuantities());
    final boolean allProductsAvailable = productIdsNotInStock.isEmpty();

    if (allProductsAvailable) {
      final OrderConfirmed orderConfirmed = order.confirmOrder();

      eventStore.save(orderConfirmed.getAggregateId(), orderConfirmed);
    } else {
      final FailedToPutHoldOnProducts failedToPutHoldOnOrder = order.failToPutHoldOnProducts(productIdsNotInStock);

      eventStore.save(failedToPutHoldOnOrder.getAggregateId(), failedToPutHoldOnOrder);
    }
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_POSTED_BY_CUSTOMER_1_0;
  }
}