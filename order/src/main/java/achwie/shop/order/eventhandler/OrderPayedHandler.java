package achwie.shop.order.eventhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.event.OrderPayed;
import achwie.shop.order.event.OrderShipped;
import achwie.shop.order.write.OrderEventPublisher;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderPayedHandler implements EventHandler<OrderPayed> {
  private final OrderEventPublisher orderEventPublisher;

  @Autowired
  public OrderPayedHandler(EventHandlerChain handlerChain, OrderEventPublisher orderEventPublisher) {
    this.orderEventPublisher = orderEventPublisher;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderPayed event) {
    final String orderId = event.getOrderId();

    // TODO: Handle shipment
    System.out.println("Order shipped " + orderId);

    final OrderShipped orderShipped = new OrderShipped(orderId);

    orderEventPublisher.publish(orderShipped);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_PAYED_1_0;
  }

}
