package achwie.shop.order.write.eventhandler;

import java.time.ZonedDateTime;
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
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.event.OrderConfirmed;
import achwie.shop.order.write.event.OrderPayed;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderConfirmedHandler implements EventHandler<OrderConfirmed> {
  private static final Logger LOG = LoggerFactory.getLogger(OrderConfirmedHandler.class);
  private final EventStore eventStore;

  @Autowired
  public OrderConfirmedHandler(EventHandlerChain handlerChain, EventStore eventStore) {
    this.eventStore = eventStore;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderConfirmed event) {
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    // TODO: Load payment data for customer
    LOG.info("TODO: Loaded payment data for customer {}", order.getUserId());
    // TODO: Handle payment
    LOG.info("TODO: Handled payment for order {}", order.getId());
    // TODO: Notify fulfillment to ship order (but: wouldn't fulfillment just
    // also listen for an OrderPayed event?)
    LOG.info("TODO: Notified fulfillment to ship order {}", order.getId());

    final ZonedDateTime payedTime = ZonedDateTime.now();

    final OrderPayed orderPayed = order.payOrder(payedTime);

    eventStore.save(orderPayed.getAggregateId(), orderPayed);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_CONFIRMED_1_0;
  }

}
