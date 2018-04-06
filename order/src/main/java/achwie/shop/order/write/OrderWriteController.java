package achwie.shop.order.write;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.eventstore.EventStore;
import achwie.shop.order.AuthService;
import achwie.shop.order.event.OrderPostedByCustomer;
import achwie.shop.order.store.write.IdGenerator;
import achwie.shop.order.store.write.MutableOrder;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@RestController
@RequestMapping("/orders")
public class OrderWriteController {
  private static final ResponseEntity<String> RESPONSE_SUCCESS = new ResponseEntity<String>("OK", HttpStatus.OK);
  private final EventStore eventStore;
  private final OrderEventPublisher orderEventPublisher;
  private final IdGenerator idGenerator;
  private final AuthService authService;

  @Autowired
  public OrderWriteController(EventStore eventStore, OrderEventPublisher orderWriteService, IdGenerator idGenerator, AuthService authService) {
    this.eventStore = eventStore;
    this.orderEventPublisher = orderWriteService;
    this.idGenerator = idGenerator;
    this.authService = authService;
  }

  @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
  public ResponseEntity<String> placeOrder(@PathVariable String sessionId, @RequestBody Cart cart) {
    final String sessionUserId = authService.getUserIdForSession(sessionId);

    if (sessionUserId == null)
      return new ResponseEntity<String>("Insufficient privileges for operation!", HttpStatus.UNAUTHORIZED);

    if (cart == null || cart.isEmpty())
      return new ResponseEntity<String>("ERROR: Can't send an empty order!.", HttpStatus.BAD_REQUEST);

    postOrder(sessionUserId, cart);

    return RESPONSE_SUCCESS;
  }

  private void postOrder(String userId, Cart cart) {
    final int itemCount = cart.getItems().size();
    final ZonedDateTime orderTime = ZonedDateTime.now();
    final String[] productIds = new String[itemCount];
    final int[] quantities = new int[itemCount];

    for (int i = 0; i < itemCount; i++) {
      final CartItem item = cart.getItems().get(i);
      productIds[i] = item.getProductId();
      quantities[i] = item.getQuantity();
    }

    final MutableOrder order = new MutableOrder();
    final String newOrderId = idGenerator.nextOrderId();
    final OrderPostedByCustomer orderPosted = order.postOrder(newOrderId, userId, orderTime, productIds, quantities);

    eventStore.save(orderPosted.getAggregateId(), orderPosted);
  }
}
