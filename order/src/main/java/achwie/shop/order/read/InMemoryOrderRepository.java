package achwie.shop.order.read;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.write.domain.MutableOrder;
import achwie.shop.order.write.domain.MutableOrderItem;
import achwie.shop.order.write.eventhandler.EventVersions;

/**
 * A very simple in-memory repository for querying orders.
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Component
public class InMemoryOrderRepository implements OrderReadRepository {
  private static final Logger LOG = LoggerFactory.getLogger(InMemoryOrderRepository.class);
  private final Object lock = new Object();
  private final Map<String, List<OrderDto>> orders = new HashMap<>();
  private final EventStore eventStore;

  @Autowired
  public InMemoryOrderRepository(EventHandlerChain handlerChain, EventStore eventStore) {
    this.eventStore = eventStore;

    // Listen to order events to update read projection
    for (EventVersions eventVersion : EventVersions.allVersions())
      handlerChain.addEventHandler(new AggregateChangedHandler(eventVersion));
  }

  public List<OrderDto> getOrdersForUser(String userId) {
    if (userId == null)
      return Collections.emptyList();

    synchronized (lock) {
      final List<OrderDto> ordersForUser = orders.get(userId);

      if (ordersForUser == null)
        return Collections.emptyList();

      return ordersForUser;
    }
  }

  public void save(OrderDto order) {
    final String userId = order.getUserId();

    // Use a global lock across all customers for simplicity
    synchronized (lock) {
      List<OrderDto> ordersForUser = orders.get(userId);
      if (ordersForUser == null) {
        ordersForUser = new ArrayList<>();
        orders.put(userId, ordersForUser);
      } else {
        // Probably this order already exists (if so, replace by new one)
        final OrderDto currentOrder = get(userId, order.getId());
        if (currentOrder != null) {
          ordersForUser.remove(currentOrder);
        }
      }

      ordersForUser.add(order);

      LOG.debug("Added order for user (userId: {}): {}", userId, order);

      // Sort on save (assume more reads than writes)
      Collections.sort(ordersForUser, new Comparator<OrderDto>() {
        @Override
        public int compare(OrderDto o1, OrderDto o2) {
          return o1.getOrderTime().compareTo(o2.getOrderTime());
        }
      });
    }
  }

  private OrderDto get(String customerId, String orderId) {
    List<OrderDto> ordersForUser = orders.get(customerId);
    return ordersForUser.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
  }

  private void onAggregateChanged(DomainEvent event) {
    if (event == null)
      return;

    LOG.debug("Received event for aggregate: {}", event);

    // Really simple: load latest order and replace old version with it
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    final OrderDto mappedOrder = map(order);

    save(mappedOrder);
  }

  private OrderDto map(MutableOrder order) {
    final String id = order.getId();
    final String userId = order.getUserId();
    final ZonedDateTime orderTime = order.getOrderTime();
    final List<OrderItemDto> mappedOrderItems = map(order.getItems());
    final String status = order.getStatus().toString();

    return new OrderDto(id, userId, orderTime, mappedOrderItems, status);
  }

  private List<OrderItemDto> map(List<MutableOrderItem> orderItems) {
    return orderItems.stream()
        .map(i -> new OrderItemDto(i.getProductId(), i.getProductName(), i.getQuantity()))
        .collect(Collectors.toList());
  }

  // ---------------------------------------------------------------------------
  private class AggregateChangedHandler implements EventHandler<DomainEvent> {
    private final EventVersion processableEventVersion;

    public AggregateChangedHandler(EventVersion processableEventVersion) {
      this.processableEventVersion = processableEventVersion;
    }

    @Override
    public void onEvent(DomainEvent event) {
      onAggregateChanged(event);
    }

    @Override
    public EventVersion getProcessableEventVersion() {
      return processableEventVersion;
    }
  }
}
