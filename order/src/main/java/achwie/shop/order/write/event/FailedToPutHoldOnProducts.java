package achwie.shop.order.write.event;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.eventstore.DomainEvent;

/**
 * 
 * @author 13.04.2018, Achim Wiedemann
 *
 */
public class FailedToPutHoldOnProducts implements DomainEvent {
  private final String orderId;
  private final List<String> productIdsNotInStock = new ArrayList<>();

  @JsonCreator
  public FailedToPutHoldOnProducts(@JsonProperty("orderId") String orderId, @JsonProperty("productIdsNotInStock") List<String> productIdsNotInStock) {
    this.orderId = orderId;
    this.productIdsNotInStock.addAll(productIdsNotInStock);
  }

  @Override
  public Object getAggregateId() {
    return orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public List<String> getProductIdsNotInStock() {
    return productIdsNotInStock;
  }

  @Override
  public String toString() {
    return String.format("%s[orderId: %s, productIdsNotInStock: %s]", getClass().getSimpleName(), orderId, productIdsNotInStock);
  }
}
