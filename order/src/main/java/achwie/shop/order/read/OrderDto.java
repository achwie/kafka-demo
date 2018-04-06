package achwie.shop.order.read;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

  @JsonCreator
  public OrderDto(@JsonProperty("id") String id, @JsonProperty("userId") String userId, @JsonProperty("orderTime") ZonedDateTime orderTime,
      @JsonProperty("orderItems") List<OrderItemDto> orderItems) {
    this.id = id;
    this.userId = userId;
    this.orderTime = orderTime;
    this.orderItems = orderItems;
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
}
