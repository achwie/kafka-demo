package achwie.shop.order.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;
import achwie.shop.event.api.EventSource;
import achwie.shop.order.event.EventHandler;
import achwie.shop.order.eventhandler.EventHandlerChain;

/**
 * 
 * @author 06.03.2018, Achim Wiedemann
 *
 */
@Component
public class EventProcessor implements Runnable {
  private static final int POLL_TIMEOUT_MILLIS = 60 * 1000;
  private final EventSource eventSource;
  private final EventHandlerChain handlerChain;
  private volatile boolean running = true;

  @Autowired
  public EventProcessor(EventSource eventSource, EventHandlerChain handlerChain) {
    this.eventSource = eventSource;
    this.handlerChain = handlerChain;

    // TODO: Use Spring's TaskExecutor
    // Submit myself (letting "this" escape to other object during construction
    // is a no-no!)
    new Thread(this, "Event processor").start();
  }

  public void run() {
    while (running) {
      final Event event = eventSource.read(POLL_TIMEOUT_MILLIS);

      System.out.println("Polled for event: " + event);
      if (event != null)
        handleEvent(event);
    }
  }

  public void stop() {
    running = false;
  }

  private void handleEvent(Event evt) {
    final EventHeader header = evt.getHeader();
    final int eventType = header.getType();
    final int eventVersion = header.getVersion();

    final EventHandler<?> handler = handlerChain.findEventHandlerFor(eventType, eventVersion);

    if (handler != null)
      handler.handle(evt);
  }
}
