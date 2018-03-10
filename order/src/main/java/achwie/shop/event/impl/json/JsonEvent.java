package achwie.shop.event.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.event.impl.pojo.PojoEvent;

/**
 * An event which consists of a header and a payload.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class JsonEvent extends PojoEvent {
  /**
   * {@inheritDoc}
   */
  @JsonCreator
  public JsonEvent(@JsonProperty("header") JsonEventHeader header, @JsonProperty("payload") Object payload) {
    super(header, payload);
  }

  /**
   * Getter for JSON (de-) serialzation.
   * 
   * @return The event's payload.
   */
  @JsonProperty
  public Object getPayload() {
    return getPayload(Object.class);
  }
}
