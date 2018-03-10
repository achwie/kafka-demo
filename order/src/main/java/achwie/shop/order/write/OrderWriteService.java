package achwie.shop.order.write;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventSink;
import achwie.shop.event.impl.EventWrapper;
import achwie.shop.order.Order;

/**
 * 
 * @author 03.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderWriteService {
  private final EventSink eventSink;
  private final EventWrapper eventWrapper;

  @Autowired
  public OrderWriteService(EventSink eventSink, EventWrapper eventWrapper) {
    this.eventSink = eventSink;
    this.eventWrapper = eventWrapper;
  }

  /**
   * Places an order.
   * 
   * @param order The order to place
   * @return Whether the placement of the order was successful or not (e.g.
   *         because an item was not on stock)
   * @throws RuntimeException If there was a problem sending the order
   */
  public void placeOrder(Order order) {
    // TODO: Use a proper event type like "PostedOrder"
    final Event evt = eventWrapper.wrap(order);

    eventSink.publish(evt);
  }
}
