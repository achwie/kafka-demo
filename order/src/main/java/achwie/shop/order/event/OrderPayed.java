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
public class OrderPayed implements DomainEvent {
  private final String orderId;
  private final ZonedDateTime payedTime;

  @JsonCreator
  public OrderPayed(@JsonProperty("orderId") String orderId, @JsonProperty("payedTime") ZonedDateTime payedTime) {
    this.orderId = orderId;
    this.payedTime = payedTime;
  }

  public Object getAggregateId() {
    return orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public ZonedDateTime getPayedTime() {
    return payedTime;
  }

  @Override
  public String toString() {
    return String.format("%s[orderId: %s, payedTime: %s]", getClass().getSimpleName(), orderId, payedTime);
  }
}
