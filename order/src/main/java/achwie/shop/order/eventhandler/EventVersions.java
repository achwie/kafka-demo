package achwie.shop.order.eventhandler;

import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.Order;

/**
 * Holds the versions for domain events.
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public class EventVersions implements EventVersion {
  private static final int TYPE_ORDER = 1;
  public static final EventVersions ORDER_1_0 = new EventVersions(TYPE_ORDER, 1, Order.class);
  private final int typeCode;
  private final int versionCode;
  private final Class<?> eventType;

  private EventVersions(int typeCode, int versionCode, Class<?> clazz) {
    this.typeCode = typeCode;
    this.versionCode = versionCode;
    this.eventType = clazz;
  }

  public int getTypeCode() {
    return typeCode;
  }

  public int getVersionCode() {
    return versionCode;
  }

  public Class<?> getEventType() {
    return eventType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
    result = prime * result + typeCode;
    result = prime * result + versionCode;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EventVersions other = (EventVersions) obj;
    if (eventType == null) {
      if (other.eventType != null)
        return false;
    } else if (!eventType.equals(other.eventType))
      return false;
    if (typeCode != other.typeCode)
      return false;
    if (versionCode != other.versionCode)
      return false;
    return true;
  }
}
