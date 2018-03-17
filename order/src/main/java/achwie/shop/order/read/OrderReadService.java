package achwie.shop.order.read;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.order.store.read.Order;
import achwie.shop.order.store.read.OrderReadRepository;

/**
 * 
 * @author 03.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderReadService {
  private final OrderReadRepository orderRepo;

  @Autowired
  public OrderReadService(OrderReadRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

  public List<Order> getOrdersForUser(String userId) {
    return orderRepo.getOrdersForUser(userId);
  }
}
