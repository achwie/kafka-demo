package achwie.shop.order.write.domain;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.order.ProductDetails;
import achwie.shop.order.write.event.OrderConfirmed;
import achwie.shop.order.write.event.OrderPayed;
import achwie.shop.order.write.event.OrderPostedByCustomer;
import achwie.shop.order.write.event.OrderShipped;

/**
 * 
 * @author 10.04.2018, Achim Wiedemann
 *
 */
public class MutableOrderTest {
  private static final String ORDER_ID = "shop://orders/1";
  private static final String USER_ID = "shop://users/1";
  private static final ZonedDateTime NOW = ZonedDateTime.now();
  private static final ZonedDateTime PAYED_TIME = NOW.plusMinutes(2);
  private static final ZonedDateTime SHIPPED_TIME = NOW.plusHours(16);

  @Test
  public void postOrder_shouldInitializeOrder() {
    final MutableOrder order = new MutableOrder();

    postOrder(order);

    assertEquals(ORDER_ID, order.getId());
    assertEquals(NOW, order.getOrderTime());
    assertEquals(USER_ID, order.getUserId());
    assertEquals(OrderStatus.REGISTERED, order.getStatus());
    assertEquals("1", order.getItems().get(0).getProductId());
    assertEquals(2, order.getItems().get(0).getQuantity());
    assertEquals("First", order.getItems().get(0).getProductName());
    assertEquals("2", order.getItems().get(1).getProductId());
    assertEquals(1, order.getItems().get(1).getQuantity());
    assertEquals("Second", order.getItems().get(1).getProductName());
  }

  @Test
  public void confirmOrder_shouldSetProductDetailsAndOrderStatusConfirmed() {
    final MutableOrder order = new MutableOrder();

    postOrder(order);
    confirmOrder(order);

    assertEquals(OrderStatus.CONFIRMED, order.getStatus());
  }

  @Test
  public void payOrder() {
    final MutableOrder order = new MutableOrder();

    postOrder(order);
    confirmOrder(order);
    payOrder(order);

    assertEquals(OrderStatus.PAYED, order.getStatus());
  }

  @Test
  public void shipOrder_shouldSetOrderStatusShipped() {
    final MutableOrder order = new MutableOrder();

    postOrder(order);
    confirmOrder(order);
    payOrder(order);
    shipOrder(order);

    assertEquals(OrderStatus.SHIPPED, order.getStatus());
  }

  @Test
  public void MutableOrder_history_shouldEqualOrderWithSameActions() {
    final List<DomainEvent> history = new ArrayList<>();
    final MutableOrder order = new MutableOrder();

    history.add(postOrder(order));
    history.add(confirmOrder(order));
    history.add(payOrder(order));
    history.add(shipOrder(order));

    final MutableOrder orderFromHistory = new MutableOrder(history);

    // Quick'n'dirty (don't wanna override hashCode and equals just for test)...
    assertEquals(order.toString(), orderFromHistory.toString());
  }

  // -- End of Tests -----------------------------------------------------------
  private ProductDetails productDetails(String productId, String productName) {
    return new ProductDetails(productId, productName);
  }

  private OrderPostedByCustomer postOrder(MutableOrder order) {
    return order.postOrder(ORDER_ID, USER_ID, NOW, new String[] { "1", "2" }, new int[] { 2, 1 },
        new ProductDetails[] { productDetails("1", "First"), productDetails("2", "Second") });
  }

  private OrderConfirmed confirmOrder(MutableOrder order) {
    return order.confirmOrder();
  }

  private OrderPayed payOrder(MutableOrder order) {
    return order.payOrder(PAYED_TIME);
  }

  private OrderShipped shipOrder(MutableOrder order) {
    return order.shipOrder(SHIPPED_TIME);
  }
}
