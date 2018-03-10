package achwie.shop.order.read;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.order.AuthService;
import achwie.shop.order.Order;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@RestController
@RequestMapping("/orders")
public class OrderReadController {
  private final OrderReadService orderReadService;
  private final AuthService authService;

  @Autowired
  public OrderReadController(OrderReadService orderReadService, AuthService authService) {
    this.orderReadService = orderReadService;
    this.authService = authService;
  }

  @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
  public ResponseEntity<List<Order>> viewOrders(@PathVariable String sessionId) {
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    final List<Order> orders;
    if (sessionUserId != null) {
      orders = orderReadService.getOrdersForUser(sessionUserId);
    } else {
      orders = Collections.emptyList();
    }

    return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
  }
}
