package achwie.shop.event.impl.pojo;

import achwie.shop.event.api.EventHeader;

/**
 * Carries metadata about an {@link PojoEvent} such as <em>versionCode code</em> and
 * <em>typeCode code</em>. Note that <em>versionCode code</em> is best managed
 * separately for each <em>typeCode code</em>. These integers must be managed and
 * set by the application.
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public class PojoEventHeader implements EventHeader {
  private final int versionCode;
  private final int typeCode;

  /**
   * Creates an event header with some basic information.
   * 
   * @param typeCode The event typeCode code
   * @param versionCode The event versionCode code
   */
  public PojoEventHeader(int typeCode, int versionCode) {
    this.versionCode = versionCode;
    this.typeCode = typeCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getVersionCode() {
    return versionCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTypeCode() {
    return typeCode;
  }
}
