package achwie.shop.event.impl;

import achwie.shop.event.api.Event;

/**
 * Serializes and deserializes events for/from transit
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public interface EventSerializer {

  /**
   * Serializes and event for transit.
   * 
   * @param evt The event to serialize - must not be {@code null}
   * @return The serialized event - never {@code null}
   * @throws SerializationException If there was a problem during serialization
   */
  public String serialize(Event evt) throws SerializationException;

  /**
   * Deserializes and event from transit.
   * 
   * @param value The serialized event to reconstitute - must not be
   *          {@code null}
   * @return The deserialized object - never {@code null}
   * @throws SerializationException If there was a problem during
   *           deserialization
   */
  public Event deserialize(String value) throws SerializationException;

}