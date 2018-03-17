package achwie.shop.order.read;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
// TODO: Add shipping address
public class OrderDto {
  private final String userId;
  private final List<OrderItemDto> orderItems = new ArrayList<>();
  private final Calendar orderDate = Calendar.getInstance();

  @JsonCreator
  public OrderDto(@JsonProperty("userId") String userId) {
    this.userId = userId;
  }

  public void addOrderItem(OrderItemDto item) {
    orderItems.add(item);
  }

  public List<OrderItemDto> getOrderItems() {
    return Collections.unmodifiableList(orderItems);
  }

  public Calendar getOrderDate() {
    final Calendar copy = Calendar.getInstance();

    copy.setTimeInMillis(orderDate.getTimeInMillis());
    copy.setTimeZone(orderDate.getTimeZone());

    return copy;
  }

  public String getUserId() {
    return userId;
  }
}
