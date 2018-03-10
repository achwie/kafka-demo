package achwie.shop.event.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.event.impl.pojo.PojoEventHeader;

/**
 * Carries metadata about an {@link JsonEvent} such as <em>version code</em> and
 * <em>type code</em>. Note that <em>version code</em> is best managed
 * separately for each <em>type code</em>. These integers must be managed and
 * set by the application.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class JsonEventHeader extends PojoEventHeader {

  /**
   * Creates an event header with some basic information.
   * 
   * @param typeCode The event type code
   * @param versionCode The event version code
   */
  @JsonCreator
  public JsonEventHeader(@JsonProperty("typeCode") int typeCode, @JsonProperty("versionCode") int versionCode) {
    super(typeCode, versionCode);
  }
}
