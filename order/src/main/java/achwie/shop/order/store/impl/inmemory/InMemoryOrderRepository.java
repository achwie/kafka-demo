package achwie.shop.order.store.impl.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.order.store.read.Order;
import achwie.shop.order.store.write.MutableOrder;
import achwie.shop.order.store.write.OrderReadWriteRepository;
import achwie.shop.order.store.write.OrderStatus;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Component
public class InMemoryOrderRepository implements OrderReadWriteRepository {
  private final EnumSet<OrderStatus> PUBLIC_ORDER_STATES = EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.SHIPPED);
  private final IdGenerator idGenerator;
  private final Object lock = new Object();
  private final Map<String, List<MutableOrder>> orders = new HashMap<>();

  @Autowired
  public InMemoryOrderRepository(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  public List<Order> getOrdersForUser(String userId) {
    if (userId == null)
      return Collections.emptyList();

    synchronized (lock) {
      final List<? extends Order> ordersForUser = orders.get(userId);

      if (ordersForUser == null)
        return Collections.emptyList();

      return ordersForUser.stream()
          .filter(o -> PUBLIC_ORDER_STATES.contains(o.getStatus()))
          .collect(Collectors.toList());
    }
  }

  @Override
  public void save(MutableOrder order) {
    if (order.getId() != null)
      throw new UnsupportedOperationException("Updates not implemented yet!");

    final String orderId = idGenerator.nextId();
    order.setId(orderId);

    final String userId = order.getUserId();

    synchronized (lock) {
      List<MutableOrder> ordersForUser = orders.get(userId);
      if (ordersForUser == null) {
        ordersForUser = new ArrayList<>();
        orders.put(userId, ordersForUser);
      }

      ordersForUser.add(order);
    }
  }

  @Override
  public MutableOrder get(String customerId, String orderId) {
    List<MutableOrder> ordersForUser = orders.get(customerId);
    return ordersForUser.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
  }
}
