package achwie.shop.order.write.event;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.order.ProductDetails;

/**
 * An order posted by the customer.
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class OrderPostedByCustomer implements DomainEvent {
  private final String orderId;
  private final String userId;
  private final ZonedDateTime orderTime;
  private final String[] productIds;
  private final int[] quantities;
  private final ProductDetails[] productDetails;

  @JsonCreator
  public OrderPostedByCustomer(
      @JsonProperty("orderId") String orderId,
      @JsonProperty("userId") String userId,
      @JsonProperty("orderTime") ZonedDateTime orderTime,
      @JsonProperty("productIds") String[] productIds,
      @JsonProperty("quantities") int[] quantities,
      @JsonProperty("productDetails") ProductDetails[] productDetails) {
    this.orderId = orderId;
    this.userId = userId;
    this.orderTime = orderTime;
    this.productIds = (productIds != null) ? productIds : new String[0];
    this.quantities = (quantities != null) ? quantities : new int[0];
    this.productDetails = productDetails;
  }

  public Object getAggregateId() {
    return orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getUserId() {
    return userId;
  }

  public ZonedDateTime getOrderTime() {
    return orderTime;
  }

  public String[] getProductIds() {
    return productIds;
  }

  public int[] getQuantities() {
    return quantities;
  }

  public ProductDetails[] getProductDetails() {
    return productDetails;
  }

  @Override
  public String toString() {
    return String.format("%s[orderId: %s, usedId: %s, orderTime: %s, productIds: [length:%d], quantities [length:%d], productDetails: %s]",
        getClass().getSimpleName(), orderId,
        userId,
        orderTime, productIds.length, quantities.length, productDetails);
  }
}
