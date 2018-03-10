package achwie.shop.order;

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
public class Order {
  private final String userId;
  private final List<OrderItem> orderItems = new ArrayList<>();
  private final Calendar orderDate = Calendar.getInstance();

  // TODO: Don't want serialization info in my domain classes
  @JsonCreator
  public Order(@JsonProperty("userId") String userId) {
    this.userId = userId;
  }

  public void addOrderItem(OrderItem item) {
    orderItems.add(item);
  }

  public List<OrderItem> getOrderItems() {
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
