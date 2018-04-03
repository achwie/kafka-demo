package achwie.shop.order.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderPayed implements DomainEvent {
  private final String orderId;

  @JsonCreator
  public OrderPayed(@JsonProperty("orderId") String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }
}
