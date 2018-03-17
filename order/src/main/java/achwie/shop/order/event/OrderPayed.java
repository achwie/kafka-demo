package achwie.shop.order.event;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderPayed implements DomainEvent {
  private final String orderId;

  public OrderPayed(String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }
}
