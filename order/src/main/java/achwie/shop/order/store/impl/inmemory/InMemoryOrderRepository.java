package achwie.shop.order.store.impl.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.order.eventhandler.EventVersions;
import achwie.shop.order.store.read.Order;
import achwie.shop.order.store.read.OrderReadRepository;
import achwie.shop.order.store.write.MutableOrder;
import achwie.shop.order.store.write.OrderStatus;

/**
 * A very simple in-memory repository for querying orders.
 * 
 * @author 20.11.2015, Achim Wiedemann
 */
@Component
public class InMemoryOrderRepository implements OrderReadRepository {
  private final EnumSet<OrderStatus> PUBLIC_ORDER_STATES = EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.SHIPPED);
  private final Object lock = new Object();
  private final Map<String, List<MutableOrder>> orders = new HashMap<>();
  private final EventStore eventStore;

  @Autowired
  public InMemoryOrderRepository(EventHandlerChain handlerChain, EventStore eventStore) {
    this.eventStore = eventStore;

    // Listen to order events to update read projection
    for (EventVersions eventVersion : EventVersions.allVersions())
      handlerChain.addEventHandler(new AggregateChangedHandler(eventVersion));
  }

  public List<Order> getOrdersForUser(String userId) {
    if (userId == null)
      return Collections.emptyList();

    synchronized (lock) {
      final List<? extends Order> ordersForUser = orders.get(userId);

      if (ordersForUser == null)
        return Collections.emptyList();

      return ordersForUser.stream()
          .filter(o -> PUBLIC_ORDER_STATES.contains(o.getStatus()))
          .collect(Collectors.toList());
    }
  }

  public void save(MutableOrder order) {
    final String userId = order.getUserId();

    // Use a global lock across all customers for simplicity
    synchronized (lock) {
      List<MutableOrder> ordersForUser = orders.get(userId);
      if (ordersForUser == null) {
        ordersForUser = new ArrayList<>();
        orders.put(userId, ordersForUser);
      } else {
        // Probably this order already exists (if so, replace by new one)
        final MutableOrder currentOrder = get(userId, order.getId());
        if (currentOrder != null) {
          ordersForUser.remove(currentOrder);
        }
      }

      ordersForUser.add(order);

      // Sort on save (assume more reads than writes)
      Collections.sort(ordersForUser, new Comparator<MutableOrder>() {
        @Override
        public int compare(MutableOrder o1, MutableOrder o2) {
          return o1.getOrderTime().compareTo(o2.getOrderTime());
        }
      });
    }
  }

  private MutableOrder get(String customerId, String orderId) {
    List<MutableOrder> ordersForUser = orders.get(customerId);
    return ordersForUser.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
  }

  private void onAggregateChanged(DomainEvent event) {
    if (event == null)
      return;

    // Really simple: load latest order and replace old version with it
    final List<DomainEvent> orderHistory = eventStore.load(event.getAggregateId());
    final MutableOrder order = new MutableOrder(orderHistory);

    save(order);
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
