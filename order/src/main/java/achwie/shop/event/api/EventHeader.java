package achwie.shop.event.api;

/**
 * Carries metadata about an {@link Event} such as <em>version</em> and
 * <em>type</em>. Note that <em>version</em> is best managed separately for each
 * <em>type</em>. These integers must be managed and set by the application.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventHeader {

  /**
   * Returns the version of the event.
   * 
   * @return The version of the event
   */
  int getVersion();

  /**
   * Returns the type of the event.
   * 
   * @return The type of the event
   */
  int getType();

}