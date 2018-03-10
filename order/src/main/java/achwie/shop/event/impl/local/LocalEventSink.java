package achwie.shop.event.impl.local;

import java.util.Objects;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventSink;

/**
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public class LocalEventSink implements EventSink {
  private final LocalEventSource localEventSource;

  public LocalEventSink(LocalEventSource localEventsource) {
    this.localEventSource = localEventsource;
  }

  @Override
  public boolean publish(Event evt) {
    Objects.requireNonNull(evt, "Given event must not be null!");

    localEventSource.put(evt);
    return true;
  }
}
