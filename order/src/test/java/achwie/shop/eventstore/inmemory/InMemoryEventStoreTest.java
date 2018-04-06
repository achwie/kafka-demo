package achwie.shop.eventstore.inmemory;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.order.write.event.OrderPayed;

/**
 * 
 * @author 06.04.2018, Achim Wiedemann
 *
 */
public class InMemoryEventStoreTest {
  private static final String ORDER_ID = "1";
  private static final ZonedDateTime NOW = ZonedDateTime.now();
  private static OrderPayed ORDER_PAYED = new OrderPayed(ORDER_ID, NOW);
  private final InMemoryEventStore eventStore = new InMemoryEventStore();

  @Test
  public void eventStore_created_isEmpty() {
    final List<DomainEvent> emptyHistory = eventStore.load(ORDER_PAYED.getAggregateId());

    assertEquals(0, emptyHistory.size());
  }

  @Test
  public void save_event_shouldSaveEvent() {
    eventStore.save(ORDER_PAYED.getAggregateId(), ORDER_PAYED);

    final List<DomainEvent> orderHistory = eventStore.load(ORDER_PAYED.getAggregateId());

    assertEquals(1, orderHistory.size());
    assertEquals(ORDER_PAYED, orderHistory.get(0));
  }
}
