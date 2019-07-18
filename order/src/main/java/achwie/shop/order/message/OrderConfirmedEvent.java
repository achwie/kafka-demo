package achwie.shop.order.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 18.07.2019, Achim Wiedemann
 *
 */
public class OrderConfirmedEvent {
  private final String orderId;

  @JsonCreator
  public OrderConfirmedEvent(@JsonProperty("orderId") String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }
}
