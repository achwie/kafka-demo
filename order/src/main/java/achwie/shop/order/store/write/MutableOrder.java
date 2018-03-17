package achwie.shop.order.store.write;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import achwie.shop.order.store.read.Order;
import achwie.shop.order.store.read.OrderItem;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class MutableOrder implements Order {
  private String id;
  private OrderStatus status;
  private String userId;
  private ZonedDateTime orderTime;
  private List<MutableOrderItem> items = Collections.emptyList();

  public MutableOrder(String id, OrderStatus status, String userId, ZonedDateTime orderTime, List<MutableOrderItem> items) {
    this.id = id;
    this.status = status;
    this.userId = userId;
    this.orderTime = orderTime;
    this.items = items;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public ZonedDateTime getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(ZonedDateTime orderTime) {
    this.orderTime = orderTime;
  }

  public List<OrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void setItems(List<MutableOrderItem> items) {
    this.items = items;
  }

  public String toString() {
    return String.format("MutableOrder[id: %s, userId: %s, status: %s, items: %d]", id, userId, status, items.size());
  }
}
