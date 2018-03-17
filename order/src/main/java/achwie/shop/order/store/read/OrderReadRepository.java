package achwie.shop.order.store.read;

import java.util.List;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface OrderReadRepository {
  public List<Order> getOrdersForUser(String userId);
}
