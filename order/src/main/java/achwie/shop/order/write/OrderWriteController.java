package achwie.shop.order.write;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.order.AuthService;
import achwie.shop.order.Cart;
import achwie.shop.order.CartItem;
import achwie.shop.order.Order;
import achwie.shop.order.OrderItem;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@RestController
@RequestMapping("/orders")
public class OrderWriteController {
  private static final ResponseEntity<String> RESPONSE_SUCCESS = new ResponseEntity<String>("OK", HttpStatus.OK);
  private final OrderWriteService orderWriteService;
  private final AuthService authService;

  @Autowired
  public OrderWriteController(OrderWriteService orderWriteService, AuthService authService) {
    this.orderWriteService = orderWriteService;
    this.authService = authService;
  }

  @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
  public ResponseEntity<String> placeOrder(@PathVariable String sessionId, @RequestBody Cart cart) {
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    if (sessionUserId == null)
      return new ResponseEntity<String>("Insufficient privileges for operation!", HttpStatus.UNAUTHORIZED);

    if (cart == null || cart.isEmpty())
      return new ResponseEntity<String>("ERROR: Can't send an empty order!.", HttpStatus.BAD_REQUEST);

    final Order order = createOrderFromCart(sessionUserId, cart);

    orderWriteService.placeOrder(order);

    return RESPONSE_SUCCESS;
  }

  private Order createOrderFromCart(String userId, Cart cart) {
    final Order order = new Order(userId);

    for (CartItem cartItem : cart.getItems())
      order.addOrderItem(createOrderItemFromCartItem(cartItem));

    return order;
  }

  private OrderItem createOrderItemFromCartItem(CartItem cartItem) {
    return new OrderItem(cartItem.getProductId(), cartItem.getProductName(), cartItem.getQuantity());
  }
}
