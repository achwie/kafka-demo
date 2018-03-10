package achwie.shop.order.eventhandler;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.Order;
import achwie.shop.order.read.InMemoryOrderRepository;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class OrderEventHandler implements EventHandler<Order> {
  private final InMemoryOrderRepository orderRepo;

  public OrderEventHandler(InMemoryOrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

  @Override
  public void onEvent(Order order) {
    orderRepo.addOrder(order);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_1_0;
  }
}
