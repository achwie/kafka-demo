package achwie.shop.order.read;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.order.AuthService;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@RestController
@RequestMapping("/orders")
public class OrderReadController {
  private static final Logger LOG = LoggerFactory.getLogger(OrderReadController.class);
  private final OrderReadRepository orderReadRepo;
  private final AuthService authService;

  @Autowired
  public OrderReadController(OrderReadRepository orderReadRepo, AuthService authService) {
    this.orderReadRepo = orderReadRepo;
    this.authService = authService;
  }

  @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
  public ResponseEntity<List<OrderDto>> viewOrders(@PathVariable String sessionId) {
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    final List<OrderDto> orders;
    if (sessionUserId != null) {
      orders = orderReadRepo.getOrdersForUser(sessionUserId);
    } else {
      orders = Collections.emptyList();
    }

    LOG.debug("Returning orders for user (user-id: {}): {}", sessionUserId, orders);

    return new ResponseEntity<List<OrderDto>>(orders, HttpStatus.OK);
  }
}
