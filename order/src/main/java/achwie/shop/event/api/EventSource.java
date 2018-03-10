package achwie.shop.event.api;

/**
 * An event source from which events can be read.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventSource {

  /**
   * Returns the next available event.
   * 
   * @param timeoutMillis The max. time to wait for an event to arrive in
   *          milliseconds
   * @return The next available event - may be {@code null} on a timeout
   */
  public Event read(int timeoutMillis);
}
