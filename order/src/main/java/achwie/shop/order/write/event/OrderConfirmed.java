package achwie.shop.order.write.event;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.order.ProductDetails;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderConfirmed implements DomainEvent {
  private final String orderId;
  private final List<ProductDetails> productDetails;

  @JsonCreator
  public OrderConfirmed(@JsonProperty("orderId") String orderId, @JsonProperty("productDetails") List<ProductDetails> productDetails) {
    this.orderId = orderId;
    this.productDetails = productDetails;
  }

  public Object getAggregateId() {
    return orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public List<ProductDetails> getProductDetails() {
    return productDetails;
  }

  @Override
  public String toString() {
    return String.format("%s[orderId: %s]", getClass().getSimpleName(), orderId);
  }
}
