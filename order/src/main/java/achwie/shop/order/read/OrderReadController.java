package achwie.shop.order.read;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.order.AuthService;
import achwie.shop.order.store.read.Order;
import achwie.shop.order.store.read.OrderItem;

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
  public ResponseEntity<List<OrderDto>> viewOrders(@PathVariable String sessionId) {
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    final List<OrderDto> orders;
    if (sessionUserId != null) {
      orders = map(orderReadService.getOrdersForUser(sessionUserId));
    } else {
      orders = Collections.emptyList();
    }

    return new ResponseEntity<List<OrderDto>>(orders, HttpStatus.OK);
  }

  private List<OrderDto> map(List<Order> orders) {
    return orders.stream().map(this::map).collect(Collectors.toList());
  }

  private OrderDto map(Order order) {

    final OrderDto orderDto = new OrderDto(order.getId());
    for (OrderItem item : order.getItems())
      orderDto.addOrderItem(new OrderItemDto(item.getProductId(), item.getProductName(), item.getQuantity()));

    return orderDto;
  }
}
