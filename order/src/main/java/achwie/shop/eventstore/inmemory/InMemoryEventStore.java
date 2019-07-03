package achwie.shop.eventstore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.eventstore.EventStoreListener;

/**
 * A simple in-memory event store. Should be able to ditch it as soon as I get
 * Kafka working as my event store.
 * 
 * @author 05.04.2018, Achim Wiedemann
 */
public class InMemoryEventStore implements EventStore {
  private final Map<Object, List<DomainEvent>> allEvents = new ConcurrentHashMap<>();
  private final List<EventStoreListener> listeners = new CopyOnWriteArrayList<>();

  @Override
  public void save(Object aggregateGuid, DomainEvent event) {
    fireOnSave(Collections.singletonList(event));
    allEvents.computeIfAbsent(aggregateGuid, key -> new ArrayList<>()).add(event);
    fireOnSaved(Collections.singletonList(event));
  }

  @Override
  public List<DomainEvent> load(Object aggregateGuid) {
    final List<DomainEvent> eventsForAggregate = allEvents.get(aggregateGuid);

    return (eventsForAggregate != null) ? Collections.unmodifiableList(eventsForAggregate) : Collections.emptyList();
  }

  @Override
  public void addListener(EventStoreListener l) {
    listeners.add(l);
  }

  @Override
  public void removeListener(EventStoreListener l) {
    listeners.remove(l);
  }

  private void fireOnSave(List<DomainEvent> events) {
    for (EventStoreListener l : listeners)
      l.onSave(events);
  }

  private void fireOnSaved(List<DomainEvent> events) {
    for (EventStoreListener l : listeners)
      l.onSaved(events);
  }
}
