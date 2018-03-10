package achwie.shop.event.impl;

/**
 * Handles domain events of a certain {@link EventVersion}.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventHandler<T> {
  /**
   * Callback for when an event of an {@link EventVersion version} arrives that
   * this handler can handle.
   * 
   * @param event The event to handle - may be {@code null}.
   */
  public void onEvent(T event);

  /**
   * Returns the {@link EventVersion version} that this handler can process.
   * 
   * @return The version that this handler can process - never {@code null}.
   */
  public EventVersion getProcessableEventVersion();
}
