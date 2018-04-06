package achwie.shop.order.write.eventhandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.write.event.OrderConfirmed;
import achwie.shop.order.write.event.OrderPayed;
import achwie.shop.order.write.event.OrderPostedByCustomer;
import achwie.shop.order.write.event.OrderShipped;

/**
 * Holds the versions for domain events.
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
// TODO: Rename to OrderEventVersions to make it clearer that this is only for
// order events
public class EventVersions implements EventVersion {
  private static final List<EventVersions> allVersions = new ArrayList<>();
  private static final int TYPE_ORDER_POSTED_BY_CUSTOMER = 1;
  private static final int TYPE_ORDER_REGISTERED = 2;
  private static final int TYPE_ORDER_PAYED = 3;
  private static final int TYPE_ORDER_SHIPPED = 4;
  public static final EventVersions ORDER_POSTED_BY_CUSTOMER_1_0 = new EventVersions(TYPE_ORDER_POSTED_BY_CUSTOMER, 1, OrderPostedByCustomer.class);
  public static final EventVersions ORDER_CONFIRMED_1_0 = new EventVersions(TYPE_ORDER_REGISTERED, 1, OrderConfirmed.class);
  public static final EventVersions ORDER_PAYED_1_0 = new EventVersions(TYPE_ORDER_PAYED, 1, OrderPayed.class);
  public static final EventVersions ORDER_SHIPPED_1_0 = new EventVersions(TYPE_ORDER_SHIPPED, 1, OrderShipped.class);
  private final int typeCode;
  private final int versionCode;
  private final Class<?> eventType;

  private EventVersions(int typeCode, int versionCode, Class<?> clazz) {
    this.typeCode = typeCode;
    this.versionCode = versionCode;
    this.eventType = clazz;

    allVersions.add(this);
  }

  public static List<EventVersions> allVersions() {
    return Collections.unmodifiableList(allVersions);
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
