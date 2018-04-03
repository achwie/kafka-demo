package achwie.shop.order.event;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An order posted by the customer.
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderPostedByCustomer implements DomainEvent {
  private final String userId;
  private final ZonedDateTime orderDate;
  private final String[] productIds;
  private final int[] quantities;

  @JsonCreator
  public OrderPostedByCustomer(
      @JsonProperty("userId") String userId,
      @JsonProperty("orderDate") ZonedDateTime orderDate,
      @JsonProperty("productIds") String[] productIds,
      @JsonProperty("quantities") int[] quantities) {
    this.userId = userId;
    this.orderDate = orderDate;
    this.productIds = productIds;
    this.quantities = quantities;
  }

  public String getUserId() {
    return userId;
  }

  public ZonedDateTime getOrderDate() {
    return orderDate;
  }

  public String[] getProductIds() {
    return productIds;
  }

  public int[] getQuantities() {
    return quantities;
  }
}
