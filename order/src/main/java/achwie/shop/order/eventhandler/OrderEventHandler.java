package achwie.shop.order.eventhandler;

import achwie.shop.event.api.Event;
import achwie.shop.order.Order;
import achwie.shop.order.event.EventHandler;
import achwie.shop.order.event.EventType;
import achwie.shop.order.event.EventVersion;
import achwie.shop.order.read.InMemoryOrderRepository;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class OrderEventHandler implements EventHandler<Order> {
  private InMemoryOrderRepository orderRepo;

  public OrderEventHandler(InMemoryOrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

  @Override
  public boolean canHandle(int eventType, int eventVersion) {
    return eventType == EventType.TYPE_ORDER && eventVersion == EventVersion.VERSION_ORDER_1_0;
  }

  @Override
  public void handle(Event evt) {
    final Order order = evt.getPayload(Order.class);

    orderRepo.addOrder(order);
  }

  @Override
  public Class<Order> getPayloadType() {
    return Order.class;
  }
}
