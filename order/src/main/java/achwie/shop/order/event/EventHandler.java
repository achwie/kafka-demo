package achwie.shop.order.event;

import achwie.shop.event.api.Event;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface EventHandler<T> {
  public boolean canHandle(int eventType, int eventVersion);

  public void handle(Event evt);

  public Class<T> getPayloadType();
}
