package achwie.shop.event.impl;

/**
 * Maps between event versions and their used event types.
 * 
 * @author 09.03.2018, Achim Wiedemann
 * @see EventVersion
 */
public interface EventTypeMapper {
  /**
   * Returns the event type for a given type / version code combination.
   * 
   * @param typeCode The type code of the event
   * @param versionCode The version code of the event
   * @return The event type for the given type / version combination or
   *         {@code null} if no type is registered for that combination.
   */
  public Class<?> getEventTypeFor(int typeCode, int versionCode);

  /**
   * Returns the event version which is used by the given event type.
   * 
   * @param eventType The type to check
   * @return The event version which is used by the given event type or
   *         {@code null} if not found.
   */
  public EventVersion getEventVersionFor(Class<?> eventType);
}
