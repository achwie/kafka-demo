package achwie.shop.order.eventhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.event.OrderConfirmed;
import achwie.shop.order.event.OrderPayed;
import achwie.shop.order.write.OrderEventPublisher;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderConfirmedHandler implements EventHandler<OrderConfirmed> {
  private final OrderEventPublisher orderEventPublisher;

  @Autowired
  public OrderConfirmedHandler(EventHandlerChain handlerChain, OrderEventPublisher orderEventPublisher) {
    this.orderEventPublisher = orderEventPublisher;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderConfirmed event) {
    final String orderId = event.getOrderId();

    // TODO: Handle payment
    System.out.println("Handled payment for order " + orderId);
    // TODO: Notify fulfillment to ship order
    System.out.println("Handled payment for order " + orderId);

    final OrderPayed orderPayed = new OrderPayed(orderId);

    orderEventPublisher.publish(orderPayed);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_CONFIRMED_1_0;
  }

}
