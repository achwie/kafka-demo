package achwie.shop.event.impl.kafka;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;

/**
 * Encapsulates the Kafka event types used for serialization.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class KafkaEventFactory {
  private final ObjectMapper objectMapper;
  private final PayloadTypeMapper payloadTypeMapper;

  public KafkaEventFactory(ObjectMapper objectMapper, PayloadTypeMapper payloadTypeMapper) {
    this.objectMapper = objectMapper;
    this.payloadTypeMapper = payloadTypeMapper;
  }

  /**
   * Creates a serializable event which can be deserialized to type
   * {@link #getEventType()}.
   * 
   * @param version The event version
   * @param type The type of the event
   * @param payload The event payload
   * @return A serializable event
   */
  public Event createEvent(int version, int type, Object payload) {
    final JsonEventHeader header = new JsonEventHeader(version, type);
    final Event evt = new JsonEvent(header, payload);

    return evt;
  }

  /**
   * The event class used for (de-) serialization.
   * 
   * @return The event class used for serialization.
   */
  public Class<? extends Event> getEventType() {
    return JsonEvent.class;
  }

  public Class<? extends EventHeader> getEventHeaderType() {
    return JsonEventHeader.class;
  }

  public Event deserialize(String value) {
    try {
      final JsonNode rootNode = objectMapper.readTree(value);
      final JsonNode headerNode = rootNode.get("header");
      final JsonNode payloadNode = rootNode.get("payload");

      final JsonEventHeader header = objectMapper.reader().treeToValue(headerNode, JsonEventHeader.class);
      final int eventType = header.getType();
      final int eventVersion = header.getVersion();

      final Class<?> payloadClass = payloadTypeMapper.getPayloadTypeFor(eventType, eventVersion);
      if (payloadClass != null) {
        final Object payload = objectMapper.reader().treeToValue(payloadNode, payloadClass);
        return new JsonEvent(header, payload);
      } else {
        // TODO: Proper handling!
        System.err.println(String.format("Couldn't find a payload type for event (event-type: %d, event-version: %d)! Discarding event!", header.getType(),
            header.getVersion()));
      }
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassCastException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // TODO: Can't find mapping for payload or exception occured - rather throw
    // exception?
    return null;
  }
}
