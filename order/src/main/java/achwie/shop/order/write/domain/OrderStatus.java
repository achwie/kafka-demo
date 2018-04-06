package achwie.shop.order.write.domain;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public enum OrderStatus {
  /**
   * The order has been registered into the system.
   */
  REGISTERED,
  /**
   * The order is confirmed (i.e. availability of all items is confirmed,
   * shipment address is available, and order has been payed for).
   */
  CONFIRMED,
  /**
   * The order has physically been shipped out and is now in transit to the
   * customer.
   */
  SHIPPED;
}
