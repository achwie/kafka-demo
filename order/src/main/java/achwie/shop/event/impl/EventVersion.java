package achwie.shop.event.impl;

/**
 * Indicates which event type a certain type code / version code combination
 * uses. An event type must only belong to a single type / version code
 * combination to have an unambiguous mapping between (type-code, version-code)
 * &lt;-&gt; (event-type).
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventVersion {
  /**
   * Returns the type code for the version.
   * 
   * @return The type code for the version
   */
  public int getTypeCode();

  /**
   * Returns the version code for the version.
   * 
   * @return The version code for the version.
   */
  public int getVersionCode();

  /**
   * Returns the Java class for a type / version code combination.
   * 
   * @return The Java class for a type / version code combination - never
   *         {@code null}
   */
  public Class<?> getEventType();
}
