package achwie.shop.order;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
public class Order {
  private final String userId;
  private final List<OrderItem> orderItems;
  private final ZonedDateTime orderDate;
  private final String status;

  @JsonCreator
  Order(@JsonProperty("userId") String userId, @JsonProperty("orderItems") List<OrderItem> orderItems, @JsonProperty("orderTime") ZonedDateTime orderTime,
      @JsonProperty("status") String status) {
    this.userId = userId;
    this.orderItems = (orderItems != null) ? orderItems : Collections.emptyList();
    this.orderDate = orderTime;
    this.status = status;
  }

  public void addOrderItem(OrderItem item) {
    orderItems.add(item);
  }

  public List<OrderItem> getOrderItems() {
    return Collections.unmodifiableList(orderItems);
  }

  public Calendar getOrderDate() {
    final Calendar copy = Calendar.getInstance();

    copy.setTimeInMillis(orderDate.toInstant().toEpochMilli());
    copy.setTimeZone(TimeZone.getTimeZone(orderDate.getZone().getId()));

    return copy;
  }

  public String getOrderDateFormatted() {
    return String.format("%TF", getOrderDate());
  }

  public Object getUserId() {
    return userId;
  }

  public String getStatus() {
    return status;
  }
}
