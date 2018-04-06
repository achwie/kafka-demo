package achwie.shop.order.read;

import java.util.List;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface OrderReadRepository {
  /**
   * Returns the order for the given user id, sorted by order time.
   * 
   * @param userId The ID of the user.
   * @return The order for the given user id, sorted by order time or an empty
   *         list if there are no orders - never {@code null}.
   */
  public List<OrderDto> getOrdersForUser(String userId);
}
