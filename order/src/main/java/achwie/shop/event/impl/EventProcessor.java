package achwie.shop.event.impl;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;
import achwie.shop.event.api.EventSource;

/**
 * Polls for events from an {@link EventSource event source} and delegates their
 * processing to the responsible handler on the given {@link EventHandlerChain
 * handler chain}. Should be run in a background thread. To stop the processor
 * the method {@link #stop()} can be used as a cooperative means to stop polling
 * and gracefully shut down the according thread.
 * 
 * @author 06.03.2018, Achim Wiedemann
 *
 */
public class EventProcessor implements Runnable {
  // Allow for graceful shutdown within a second
  private static final int POLL_TIMEOUT_MILLIS = 1000;
  private final EventSource eventSource;
  private final EventHandlerChain handlerChain;
  private final EventWrapper eventWrapper;
  private volatile boolean running = true;

  /**
   * Creats a new event processor that takes events from the given sources and
   * delegates them to their appropriate handle on the given handler chain.
   * 
   * @param eventSource The event source to take the events from
   * @param handlerChain The handler chain to delegate the events to
   * @param eventWrapper Used to unwrap the payloads from events
   */
  public EventProcessor(EventSource eventSource, EventHandlerChain handlerChain, EventWrapper eventWrapper) {
    this.eventSource = eventSource;
    this.handlerChain = handlerChain;
    this.eventWrapper = eventWrapper;
  }

  /**
   * {@inheritDoc}
   */
  public void run() {
    while (running) {
      final Event event = eventSource.read(POLL_TIMEOUT_MILLIS);

      if (event != null)
        handleEvent(event);
    }
  }

  /**
   * Gracefully stops polling for events.
   */
  public void stop() {
    running = false;
  }

  @SuppressWarnings("unchecked")
  private <T> void handleEvent(Event evt) {
    final EventHeader header = evt.getHeader();
    final int typeCode = header.getTypeCode();
    final int versionCode = header.getVersionCode();
    final Class<T> eventType = (Class<T>) handlerChain.getEventTypeFor(typeCode, versionCode);

    if (eventType != null) {
      final EventHandler<T> handler = handlerChain.findEventHandlerFor(eventType);

      if (handler != null) {
        T payload = eventWrapper.unwrap(evt, eventType);
        handler.onEvent(payload);
      } else {
        System.err.println(String.format("Couldn't find handler for event type %s!", eventType));
      }
    } else {
      System.err.println(String.format("Couldn't find Java type for event (typeCode: %d, versionCode: %d)!", typeCode, versionCode));
    }
  }
}
