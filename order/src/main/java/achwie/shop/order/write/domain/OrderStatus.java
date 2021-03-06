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
   * shipment address is available).
   */
  CONFIRMED,
  /**
   * The confirmed order has been payed for.
   */
  PAYED,
  /**
   * The payed order has physically been shipped out and is now in transit to
   * the customer.
   */
  SHIPPED,
  /**
   * The order could not be delivered.
   */
  FAILED;
}
