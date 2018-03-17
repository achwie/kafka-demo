package achwie.shop.order.event;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderShipped implements DomainEvent {
  private final String orderId;

  public OrderShipped(String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }
}
