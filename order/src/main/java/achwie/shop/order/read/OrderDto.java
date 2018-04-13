package achwie.shop.order.read;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
// TODO: Add shipping address
public class OrderDto {
  private final String id;
  private final String userId;
  private final List<OrderItemDto> orderItems;
  private final ZonedDateTime orderTime;
  private final String status;

  @JsonCreator
  public OrderDto(String id, String userId, ZonedDateTime orderTime, List<OrderItemDto> orderItems, String status) {
    this.id = id;
    this.userId = userId;
    this.orderTime = orderTime;
    this.orderItems = orderItems;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public List<OrderItemDto> getOrderItems() {
    return orderItems;
  }

  public ZonedDateTime getOrderTime() {
    return orderTime;
  }

  public String getUserId() {
    return userId;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return String.format("%s[id: %s, userId: %s, orderTime: %s, orderItems: %s, status: %s]", getClass().getSimpleName(), id, userId, orderTime, orderItems,
        status);
  }
}
