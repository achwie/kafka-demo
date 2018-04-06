package achwie.shop.order.event;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.eventstore.DomainEvent;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderShipped implements DomainEvent {
  private final String orderId;
  private final ZonedDateTime shippedTime;

  @JsonCreator
  public OrderShipped(@JsonProperty("orderId") String orderId, @JsonProperty("shippedTime") ZonedDateTime shippedTime) {
    this.orderId = orderId;
    this.shippedTime = shippedTime;
  }

  public Object getAggregateId() {
    return orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public ZonedDateTime getShippedTime() {
    return shippedTime;
  }

  @Override
  public String toString() {
    return String.format("%s[orderId: %s, shippedTime: %s]", getClass().getSimpleName(), orderId, shippedTime);
  }
}
