package achwie.shop.order.write.eventhandler;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.event.OrderPayed;
import achwie.shop.order.write.event.OrderShipped;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderPayedHandler implements EventHandler<OrderPayed> {
  private final EventStore eventStore;

  @Autowired
  public OrderPayedHandler(EventHandlerChain handlerChain, EventStore eventStore) {
    this.eventStore = eventStore;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderPayed event) {
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    // TODO: Handle shipment (wait for external signal from order fulfillment
    // that order has been dispatched)
    System.out.println("Order shipped " + order.getId());
    final ZonedDateTime shippedTime = ZonedDateTime.now();

    final OrderShipped orderShipped = order.shipOrder(shippedTime);

    eventStore.save(orderShipped.getAggregateId(), orderShipped);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_PAYED_1_0;
  }

}
