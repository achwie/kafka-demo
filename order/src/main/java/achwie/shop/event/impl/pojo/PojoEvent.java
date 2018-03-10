package achwie.shop.event.impl.pojo;

import java.util.Objects;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;

/**
 * An event which consists of a header and a payload.
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public class PojoEvent implements Event {
  private final EventHeader header;
  private final Object payload;

  /**
   * Creates a new event using the given header and payload.
   * 
   * @param header The header carrying metadata about the event - must not be
   *          {@code null}
   * @param payload The event payload - may be {@code null}
   */
  public PojoEvent(EventHeader header, Object payload) {
    Objects.requireNonNull(header, "Given header must not be null!");

    this.header = header;
    this.payload = payload;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EventHeader getHeader() {
    return header;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getPayload(Class<T> payloadType) {
    Objects.requireNonNull(payloadType, "Given payloadType must not be null!");

    if (payload == null)
      return null;

    if (payloadType.isAssignableFrom(payload.getClass()))
      return (T) payload;
    else
      throw new IllegalArgumentException(String.format("Can't cast payload of type %s to %s", payload.getClass(), payloadType));
  }
}
