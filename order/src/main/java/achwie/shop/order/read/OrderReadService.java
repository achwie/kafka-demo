package achwie.shop.order.read;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.order.Order;

/**
 * 
 * @author 03.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderReadService {
  private final InMemoryOrderRepository orderRepo;

  @Autowired
  public OrderReadService(InMemoryOrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

  public List<Order> getOrdersForUser(String userId) {
    return orderRepo.getOrdersForUser(userId);
  }
}
