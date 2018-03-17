package achwie.shop.order.eventhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.event.OrderShipped;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderShippedHandler implements EventHandler<OrderShipped> {

  @Autowired
  public OrderShippedHandler(EventHandlerChain handlerChain) {
    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderShipped event) {
    final String orderId = event.getOrderId();

    // TODO: Set order status to "shipped"
    System.out.println("Set status of order to SHIPPED for order " + orderId);
    // TODO: Notify customer via email
    System.out.println("Customer notified of shipment for order " + orderId);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_SHIPPED_1_0;
  }

}
