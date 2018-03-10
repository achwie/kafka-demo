package achwie.shop.event.api;

/**
 * An event which consists of a header and a payload.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface Event {

  /**
   * Returns the event's header.
   * 
   * @return The event's header - never {@code null}
   */
  EventHeader getHeader();

  /**
   * Returns the event's payload.
   * 
   * @param payloadType The type of the payload - must not be {@code null}
   * @return The event's payload - may be {@code null}
   */
  <T> T getPayload(Class<T> payloadType);

}