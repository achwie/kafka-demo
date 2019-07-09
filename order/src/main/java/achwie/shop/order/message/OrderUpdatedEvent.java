package achwie.shop.order.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 09.07.2019, Achim Wiedemann
 *
 */
public class OrderUpdatedEvent {
  private final String userId;
  private final String orderId;

  @JsonCreator
  public OrderUpdatedEvent(@JsonProperty("userId") String userId, @JsonProperty("orderId") String orderId) {
    this.userId = userId;
    this.orderId = orderId;
  }

  public String getUserId() {
    return userId;
  }

  public String getOrderId() {
    return orderId;
  }
}
