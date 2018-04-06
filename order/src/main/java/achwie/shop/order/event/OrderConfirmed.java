package achwie.shop.order.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.eventstore.DomainEvent;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderConfirmed implements DomainEvent {
  private final String orderId;

  @JsonCreator
  public OrderConfirmed(@JsonProperty("orderId") String orderId) {
    this.orderId = orderId;
  }

  public Object getAggregateId() {
    return orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  @Override
  public String toString() {
    return String.format("%s[orderId: %s]", getClass().getSimpleName(), orderId);
  }
}
