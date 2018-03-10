package achwie.shop.order.read;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import achwie.shop.order.Order;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Component
public class InMemoryOrderRepository {
  private final Object lock = new Object();
  private final Map<String, List<Order>> orders = new HashMap<>();

  public List<Order> getOrdersForUser(String userId) {
    if (userId == null)
      return Collections.emptyList();

    synchronized (lock) {
      final List<Order> ordersForUser = orders.get(userId);
      return (ordersForUser != null) ? ordersForUser : Collections.emptyList();
    }
  }

  public void addOrder(Order order) {
    final String userId = order.getUserId();

    synchronized (lock) {
      List<Order> ordersForUser = orders.get(userId);
      if (ordersForUser == null) {
        ordersForUser = new ArrayList<>();
        orders.put(userId, ordersForUser);
      }

      ordersForUser.add(order);
    }
  }
}
