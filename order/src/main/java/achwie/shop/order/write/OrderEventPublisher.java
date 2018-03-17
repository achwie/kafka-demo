package achwie.shop.order.write;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventSink;
import achwie.shop.event.impl.EventWrapper;
import achwie.shop.order.event.DomainEvent;

/**
 * 
 * @author 03.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderEventPublisher {
  private final EventSink eventSink;
  private final EventWrapper eventWrapper;

  @Autowired
  public OrderEventPublisher(EventSink eventSink, EventWrapper eventWrapper) {
    this.eventSink = eventSink;
    this.eventWrapper = eventWrapper;
  }

  public void publish(DomainEvent event) {
    final Event evt = eventWrapper.wrap(event);

    eventSink.publish(evt);
  }
}
