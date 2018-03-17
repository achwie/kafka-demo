package achwie.shop.order.event;

import java.time.ZonedDateTime;

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

  public OrderPostedByCustomer(String userId, ZonedDateTime orderDate, String[] productIds, int[] quantities) {
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
