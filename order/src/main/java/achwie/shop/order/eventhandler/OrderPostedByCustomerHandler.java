package achwie.shop.order.eventhandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.event.OrderConfirmed;
import achwie.shop.order.event.OrderPostedByCustomer;
import achwie.shop.order.store.write.MutableOrder;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderPostedByCustomerHandler implements EventHandler<OrderPostedByCustomer> {
  private final EventStore eventStore;

  @Autowired
  public OrderPostedByCustomerHandler(EventHandlerChain handlerChain, EventStore eventStore) {
    this.eventStore = eventStore;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderPostedByCustomer event) {
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    System.out.println("Received order and saved it under id " + order.getId());
    // TODO: Check availability
    System.out.println("Checked availability for order " + order.getId());
    // TODO: Get customer details (shipping address)
    System.out.println("Got customer details for order " + order.getId());
    // TODO: Get product details (prices)
    System.out.println("Got product details for order " + order.getId());

    final OrderConfirmed orderConfirmed = order.confirmOrder();

    eventStore.save(orderConfirmed.getAggregateId(), orderConfirmed);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_POSTED_BY_CUSTOMER_1_0;
  }
}
