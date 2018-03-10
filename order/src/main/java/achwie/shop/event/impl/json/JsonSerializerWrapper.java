package achwie.shop.event.impl.json;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;
import achwie.shop.event.impl.EventSerializer;
import achwie.shop.event.impl.EventTypeMapper;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.event.impl.EventWrapper;
import achwie.shop.event.impl.SerializationException;

/**
 * Encapsulates the Kafka event types used for serialization.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class JsonSerializerWrapper implements EventSerializer, EventWrapper {
  private final ObjectMapper objectMapper;
  private final EventTypeMapper eventTypeMapper;

  public JsonSerializerWrapper(ObjectMapper objectMapper, EventTypeMapper eventTypeMapper) {
    this.objectMapper = objectMapper;
    this.eventTypeMapper = eventTypeMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Event wrap(Object payload) {
    final EventVersion eventVersion = eventTypeMapper.getEventVersionFor(payload.getClass());
    if (eventVersion == null) {
      throw new IllegalStateException("Couldn't find version for type " + payload.getClass());
    }

    final JsonEventHeader header = new JsonEventHeader(eventVersion.getTypeCode(), eventVersion.getVersionCode());
    final JsonEvent event = new JsonEvent(header, payload);

    return event;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T unwrap(Event evt, Class<T> payloadType) {
    Objects.requireNonNull(evt, "Given event must not be null!");

    return (T) evt.getPayload(payloadType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize(Event evt) throws SerializationException {
    final EventHeader header = evt.getHeader();
    try {
      return objectMapper.writeValueAsString(evt);
    } catch (JsonProcessingException e) {
      final String msg = String.format("Could not serialize payload! (event-type: %d, event-version: %d)", header.getType(), header.getVersion());
      throw new SerializationException(msg, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Event deserialize(String value) throws SerializationException {
    try {
      final JsonNode rootNode = objectMapper.readTree(value);
      final JsonNode headerNode = rootNode.get("header");
      final JsonNode payloadNode = rootNode.get("payload");

      try {
        final JsonEventHeader header = objectMapper.reader().treeToValue(headerNode, JsonEventHeader.class);
        final int typeCode = header.getType();
        final int versionCode = header.getVersion();

        try {
          final Class<?> payloadClass = eventTypeMapper.getEventTypeFor(typeCode, versionCode);
          if (payloadClass != null) {
            final Object payload = objectMapper.reader().treeToValue(payloadNode, payloadClass);
            return new JsonEvent(header, payload);
          } else {
            final String msg = String.format("Couldn't find a payload type for event (event-type: %d, event-version: %d)! Discarding event!", header.getType(),
                header.getVersion());
            throw new SerializationException(msg);
          }
        } catch (JsonProcessingException e) {
          final String msg = String.format("Couldn't deserialize event payload (event-type: %d, event-version: %d)! Discarding event!", header.getType(),
              header.getVersion());
          throw new SerializationException(msg);
        }
      } catch (JsonProcessingException e) {
        throw new SerializationException("Couldn't deserialize event header! Discarding event!", e);
      }
    } catch (IOException e) {
      throw new SerializationException("Failed to read value for deserialization!");
    }
  }
}
