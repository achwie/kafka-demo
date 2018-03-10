package achwie.shop.event.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.event.api.EventHeader;

/**
 * Carries metadata about an {@link JsonEvent} such as <em>version</em> and
 * <em>type</em>. Note that <em>version</em> is best managed separately for each
 * <em>type</em>. These integers must be managed and set by the application.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class JsonEventHeader implements EventHeader {
  private final int version;
  private final int type;

  /**
   * Creates an event header with some basic information.
   * 
   * @param version The event version
   * @param type The event type
   */
  @JsonCreator
  public JsonEventHeader(@JsonProperty("version") int version, @JsonProperty("type") int type) {
    this.version = version;
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getVersion() {
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getType() {
    return type;
  }
}
