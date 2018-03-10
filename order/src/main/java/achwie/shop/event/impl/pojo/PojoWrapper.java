package achwie.shop.event.impl.pojo;

import java.util.Objects;

import achwie.shop.event.api.Event;
import achwie.shop.event.impl.EventTypeMapper;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.event.impl.EventWrapper;

/**
 * Encapsulates the wrapping / unwrapping of Pojo events.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class PojoWrapper implements EventWrapper {
  private final EventTypeMapper eventTypeMapper;

  public PojoWrapper(EventTypeMapper eventTypeMapper) {
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

    final PojoEventHeader header = new PojoEventHeader(eventVersion.getVersionCode(), eventVersion.getTypeCode());
    final PojoEvent event = new PojoEvent(header, payload);

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
}
