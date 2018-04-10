package achwie.shop.order.write.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.order.ProductDetails;
import achwie.shop.order.write.event.OrderConfirmed;
import achwie.shop.order.write.event.OrderPayed;
import achwie.shop.order.write.event.OrderPostedByCustomer;
import achwie.shop.order.write.event.OrderShipped;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class MutableOrder {
  private String id;
  private OrderStatus status;
  private String userId;
  private ZonedDateTime orderTime;
  private List<MutableOrderItem> items = Collections.emptyList();

  public MutableOrder() {
    this(Collections.emptyList());
  }

  public MutableOrder(List<DomainEvent> events) {
    for (DomainEvent evt : events)
      apply(evt);
  }

  public String getId() {
    return id;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public String getUserId() {
    return userId;
  }

  public ZonedDateTime getOrderTime() {
    return orderTime;
  }

  public List<MutableOrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public OrderPostedByCustomer postOrder(String newOrderId, String userId, ZonedDateTime orderTime, String[] productIds,
      int[] quantities) {
    final OrderPostedByCustomer evt = new OrderPostedByCustomer(newOrderId, userId, orderTime, productIds, quantities);

    apply(evt);

    return evt;
  }

  public OrderConfirmed confirmOrder(List<ProductDetails> productDetails) {
    final OrderConfirmed evt = new OrderConfirmed(this.id, productDetails);

    apply(evt);

    return evt;
  }

  public OrderPayed payOrder(ZonedDateTime payedTime) {
    final OrderPayed evt = new OrderPayed(this.id, payedTime);

    apply(evt);

    return evt;
  }

  public OrderShipped shipOrder(ZonedDateTime shippedTime) {
    final OrderShipped evt = new OrderShipped(this.id, shippedTime);

    apply(evt);

    return evt;
  }

  public String toString() {
    return String.format("MutableOrder[id: %s, userId: %s, status: %s, orderTime: %s, items: %s]", id, userId, status, orderTime, items);
  }

  public void apply(DomainEvent evt) {
    Objects.requireNonNull(evt, "Given domain event must not be null!");

    final Class<?> eventType = evt.getClass();

    if (eventType == OrderPostedByCustomer.class) {
      handleOrderPostedByCustomer((OrderPostedByCustomer) evt);
    } else if (eventType == OrderConfirmed.class) {
      handleOrderConfirmed((OrderConfirmed) evt);
    } else if (eventType == OrderPayed.class) {
      handleOrderPayed((OrderPayed) evt);
    } else if (eventType == OrderShipped.class) {
      handleOrderShipped((OrderShipped) evt);
    } else {
      throw new IllegalArgumentException("Can't handle unknown event of type " + eventType.getName());
    }
  }

  private void handleOrderPostedByCustomer(OrderPostedByCustomer evt) {
    if (this.id != null)
      throw new IllegalStateException("Can't populate an already existing order! (order-id: " + this.id + ")");

    this.id = evt.getOrderId();
    this.orderTime = evt.getOrderTime();
    this.userId = evt.getUserId();
    this.items = createOrderItems(evt.getProductIds(), evt.getQuantities());
    this.status = OrderStatus.REGISTERED;
  }

  private void handleOrderConfirmed(OrderConfirmed evt) {
    if (this.id == null)
      throw new IllegalStateException("Can't confirm order for uninitialized Order! (orderId in event: " + evt.getOrderId() + ")");

    if (!this.id.equals(evt.getOrderId()))
      throw new IllegalStateException(
          String.format("Can't confirm order for different Order! (orderId: %s, orderId in event: %s)", this.id, evt.getOrderId()));

    this.status = OrderStatus.CONFIRMED;
    for (ProductDetails productDetails : evt.getProductDetails()) {
      final MutableOrderItem orderItem = getItem(productDetails.getId());
      if (orderItem != null) {
        final MutableProductDetails mutableProductDetails = new MutableProductDetails();

        mutableProductDetails.setProductName(productDetails.getName());

        orderItem.setProductDetails(mutableProductDetails);
      } else {
        throw new IllegalArgumentException(String.format("ERROR: No history for product %s which was referenced in event!", productDetails.getId()));
      }
    }
  }

  private MutableOrderItem getItem(String productId) {
    for (MutableOrderItem item : items)
      if (item.getProductId().equals(productId))
        return item;

    return null;
  }

  private void handleOrderPayed(OrderPayed evt) {
    if (this.id == null)
      throw new IllegalStateException("Can't mark uninitialized Order as payed! (orderId in event: " + evt.getOrderId() + ")");

    if (!this.id.equals(evt.getOrderId()))
      throw new IllegalStateException(
          String.format("Can't mark different Order as payed! (orderId: %s, orderId in event: %s)", this.id, evt.getOrderId()));

    this.status = OrderStatus.PAYED;
  }

  private void handleOrderShipped(OrderShipped evt) {
    if (this.id == null)
      throw new IllegalStateException("Can't mark uninitialized Order as shipped! (orderId in event: " + evt.getOrderId() + ")");

    if (!this.id.equals(evt.getOrderId()))
      throw new IllegalStateException(
          String.format("Can't mark different Order as shipped! (orderId: %s, orderId in event: %s)", this.id, evt.getOrderId()));

    this.status = OrderStatus.SHIPPED;
  }

  private List<MutableOrderItem> createOrderItems(String[] orderedProductIds, int[] orderedQuantities) {
    final List<MutableOrderItem> items = new ArrayList<>();
    for (int i = 0; i < orderedProductIds.length; i++)
      items.add(new MutableOrderItem(orderedProductIds[i], orderedQuantities[i]));

    return items;
  }
}
