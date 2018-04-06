package achwie.shop.order.eventhandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.event.OrderShipped;
import achwie.shop.order.store.write.MutableOrder;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderShippedHandler implements EventHandler<OrderShipped> {
  private final EventStore eventStore;

  @Autowired
  public OrderShippedHandler(EventHandlerChain handlerChain, EventStore eventStore) {
    this.eventStore = eventStore;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderShipped event) {
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    // TODO: Set order status to "shipped"
    System.out.println("Set status of order to SHIPPED for order " + order.getId());
    // TODO: Notify customer via email
    System.out.println("Customer notified of shipment for order " + order.getId());
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_SHIPPED_1_0;
  }

}
