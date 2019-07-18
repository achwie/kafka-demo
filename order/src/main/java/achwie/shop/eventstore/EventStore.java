package achwie.shop.eventstore;

import java.util.List;

/**
 * An append-only {@link EvenStore} that stores events for their respective
 * aggregate. Aggregates <strong>must</strong> be identified by a GUID that is
 * unique to them across all records.
 * 
 * @author 05.04.2018, Achim Wiedemann
 */
public interface EventStore {
  /**
   * Saves an event that belongs to an aggregate.
   * 
   * @param aggregateGuid The GUID of the aggrate that this event belongs to
   * @param event The event to store
   */
  // TODO: Param aggregateGuid seems redundant (ID is already in DomainEvent)
  public void save(Object aggregateGuid, DomainEvent event);

  /**
   * Loads all events for an aggregate. All returned events must be treated as
   * immutable and must not be modified.
   * 
   * @param aggregateGuid The GUID of the aggrate to load the events for
   * @return A list of the events for the given GUID in the order they were
   *         stored or an empty list if there are no events for the given GUID
   */
  public List<DomainEvent> load(Object aggregateGuid);

  /**
   * Adds a listener to the event store.
   * 
   * @param l The listener to add
   */
  public void addListener(EventStoreListener l);

  /**
   * Removes a listener from the event store.
   * 
   * @param l The listener to remove
   */
  public void removeListener(EventStoreListener l);
}
