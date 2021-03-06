package achwie.shop.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import achwie.shop.cart.Cart;

/**
 * 
 * @author 20.11.2015, Achim Wiedemann
 *
 */
@Component
public class OrderService {
  private final RestTemplate restTemplate;
  private final String orderServiceBaseUrl;
  private final KafkaTemplate<String, SubmitOrderMessage> kafkaTemplate;
  private final String kafkaTopicOrder;

  @Autowired
  public OrderService(@Value("${service.order.baseurl}") String orderServiceBaseUrl, RestTemplate restTemplate,
      @Value("${kafka.topic.order}") String kafkaTopicOrder, KafkaTemplate<String, SubmitOrderMessage> kafkaTemplate) {
    this.orderServiceBaseUrl = orderServiceBaseUrl;
    this.restTemplate = restTemplate;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTopicOrder = kafkaTopicOrder;
  }

  /**
   * Places an order with the contents in the given cart for the user associated
   * with the given session ID.
   * 
   * @param sessionId The session ID to place the order for
   * @param cart The cart with the items to order
   * @return {@code true} if the order was successful, {@code false} else (e.g.
   *         because there were not enough items on stock).
   */
  public void placeOrder(String sessionId, Cart cart) {
    kafkaTemplate.send(kafkaTopicOrder, new SubmitOrderMessage(sessionId, cart));
  }

  /**
   * Returns the order history for the session's user.
   * 
   * @param sessionId The session of the user to get the orders for.
   * @return The list with orders made by the user or an empty list if the user
   *         didn't make any orders so far.
   */
  public List<Order> getOrdersForUser(String sessionId) {
    return new GetOrdersForUserCommand(restTemplate, orderServiceBaseUrl, sessionId).execute();
  }
}
