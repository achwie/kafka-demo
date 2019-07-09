package achwie.shop.order.read;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.domain.MutableOrderItem;

/**
 * A simple in-memory repository with a read-projection for querying orders.
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Component
public class InMemoryOrderRepository implements OrderReadRepository {
  private static final Logger LOG = LoggerFactory.getLogger(InMemoryOrderRepository.class);
  private final Map<String, List<OrderDto>> orders = new ConcurrentHashMap<>();
  private final EventStore eventStore;

  @Autowired
  public InMemoryOrderRepository(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public List<OrderDto> getOrdersForUser(String userId) {
    return (userId != null) ? getOrCreateOrdersForUsers(userId) : Collections.emptyList();
  }

  void update(String userId, String orderId) {
    if (userId == null || orderId == null)
      return;

    final List<OrderDto> ordersForUser = getOrCreateOrdersForUsers(userId);

    synchronized (ordersForUser) {
      final OrderDto updatedOrder = loadOrder(orderId);
      for (int i = 0; i < ordersForUser.size(); i++) {
        final OrderDto o = ordersForUser.get(i);
        if (orderId.equals(o.getId())) {
          ordersForUser.set(i, updatedOrder);
          LOG.info("Updated order {} for user {}", orderId, userId);
          return;
        }
      }
      ordersForUser.add(updatedOrder);
      LOG.info("Added order {} for user {}", orderId, userId);
    }
  }

  private List<OrderDto> getOrCreateOrdersForUsers(String userId) {
    return orders.computeIfAbsent(userId, uid -> new ArrayList<>());
  }

  private OrderDto loadOrder(String orderId) {
    final List<DomainEvent> orderEvents = eventStore.load(orderId);
    MutableOrder order = new MutableOrder(orderEvents);

    return map(order);
  }

  private OrderDto map(MutableOrder order) {
    return new OrderDto(
        order.getId(),
        order.getUserId(),
        order.getOrderTime(),
        order.getItems().stream().map(this::map).collect(Collectors.toList()),
        order.getStatus().toString());
  }

  private OrderItemDto map(MutableOrderItem orderItem) {
    return new OrderItemDto(orderItem.getProductId(), orderItem.getProductName(), orderItem.getQuantity());
  }
}
