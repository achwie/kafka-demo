package achwie.shop.eventstore;

import java.util.List;

/**
 * Listener that provides several hooks for the event store.
 * 
 * @author 05.04.2018, Achim Wiedemann
 */
public interface EventStoreListener {
  /**
   * A hook that gets called before a list of events is about to be saved. This
   * method must return immediately and not throw any exceptions.
   * 
   * @param events The events to be saved
   */
  public void onSave(List<DomainEvent> events);

  /**
   * A hook that gets called after a list of events has been saved. This method
   * must return immediately and not throw any exceptions.
   * 
   * @param events The events that have been saved.
   */
  public void onSaved(List<DomainEvent> events);
}
