package achwie.shop.event.api;

/**
 * An event sink to which events can be published.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventSink {

  /**
   * Publishes an event.
   * 
   * @param evt The event to publish - must not be {@code null}
   * @return Whether the event was published successfully
   */
  public boolean publish(Event evt);
}
