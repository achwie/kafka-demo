package achwie.shop.order.store.write;

import achwie.shop.order.store.read.OrderReadRepository;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public interface OrderReadWriteRepository extends OrderReadRepository {
  /**
   * Adds an order to the repository. If the order has not been saved before
   * (i.e. it's ID is {@code null}) it is assigned an ID.
   * 
   * @param order The order to add
   * @return The saved order
   */
  public void save(MutableOrder order);

  public MutableOrder get(String customerId, String orderId);
}
