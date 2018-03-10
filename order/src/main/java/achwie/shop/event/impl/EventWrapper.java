package achwie.shop.event.impl;

import achwie.shop.event.api.Event;

/**
 * Wraps / unwraps a payload for / from transit. In order to fill out the
 * required type / version information for the event header, the
 * {@link EventTypeMapper} is used.
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public interface EventWrapper {

  /**
   * Wraps a payload into an {@link Event} for transit.
   * 
   * @param payload The payload to wrap - must not be {@code null}
   * @return The event with the given payload - never {@code null}
   * @throws IllegalStateException If no event-type information could be found
   *           for the given payload
   */
  public Event wrap(Object payload);

  /**
   * Unwraps the payload from a given event.
   * 
   * @param evt The event to unwrap the payload from - must not be {@code null}
   * @param payloadType The target type of the payload - must not be
   *          {@code null}
   * @return The payload of the given event or {@code null} if the event didn't
   *         have any payload
   */
  public <T> T unwrap(Event evt, Class<T> payloadType);

}