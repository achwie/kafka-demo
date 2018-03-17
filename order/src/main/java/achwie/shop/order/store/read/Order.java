package achwie.shop.order.store.read;

import java.time.ZonedDateTime;
import java.util.List;

import achwie.shop.order.store.write.OrderStatus;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public interface Order {

  String getId();

  OrderStatus getStatus();

  String getUserId();

  ZonedDateTime getOrderTime();

  List<OrderItem> getItems();
}