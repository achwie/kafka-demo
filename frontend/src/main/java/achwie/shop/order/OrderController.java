package achwie.shop.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import achwie.shop.auth.SessionService;
import achwie.shop.cart.Cart;
import achwie.shop.cart.CartService;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Controller
public class OrderController {
  private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
  private final OrderService orderService;
  private final CartService cartService;
  private final SessionService sessionService;

  @Autowired
  public OrderController(OrderService orderService, CartService cartService, SessionService sessionService) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.sessionService = sessionService;
  }

  @RequestMapping(value = "order-address", method = RequestMethod.GET)
  public String enterShippingAddress() {
    sessionService.ensureAuthenticatedUser("enter shipping address");

    return "order-address";
  }

  @RequestMapping(value = "place-order", method = RequestMethod.POST)
  public String placeOrder(Model model, HttpServletRequest req) {
    sessionService.ensureAuthenticatedUser("place order");

    final String sessionId = sessionService.getSessionId();
    final Cart cart = cartService.getCart(sessionId);

    if (cart.isEmpty()) {
      LOG.warn("Can't place an order with an empty cart!");
      return "redirect:order-address";
    }

    orderService.placeOrder(sessionId, cart);

    cartService.clearCart(sessionId);
    return "redirect:order-placed";
  }

  @RequestMapping(value = "order-placed", method = RequestMethod.GET)
  public String orderPlaced() {
    return "order-placed";
  }

  @RequestMapping(value = "my-orders", method = RequestMethod.GET)
  public String viewOrders(Model model, HttpServletRequest req) {
    sessionService.ensureAuthenticatedUser("view my orders");
    final String sessionId = sessionService.getSessionId();
    final List<Order> ordersForUser = orderService.getOrdersForUser(sessionId);

    model.addAttribute("orders", ordersForUser);

    return "my-orders";
  }
}
