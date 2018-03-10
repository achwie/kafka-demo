package achwie.shop.order.read;

import java.util.List;

import achwie.shop.order.Order;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface OrderRepository {
  public List<Order> getOrdersForUser(String userId);
}
