package achwie.shop.event.api;

/**
 * Carries metadata about an {@link Event} such as <em>version code</em> and
 * <em>type code</em>. Note that <em>version code</em> is best managed
 * separately for each <em>type code</em>. These integers must be managed and
 * set by the application.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventHeader {

  /**
   * Returns the version code of the event.
   * 
   * @return The version code of the event
   */
  public int getVersionCode();

  /**
   * Returns the type code of the event.
   * 
   * @return The type code of the event
   */
  public int getTypeCode();

}