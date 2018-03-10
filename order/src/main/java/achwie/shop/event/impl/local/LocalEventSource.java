package achwie.shop.event.impl.local;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventSource;

/**
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public class LocalEventSource implements EventSource {
  private static final int BUFFER_CAPACITY = 1024;
  private final BlockingQueue<Event> eventBuffer;

  public LocalEventSource() {
    eventBuffer = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
  }

  @Override
  public Event read(int timeoutMillis) {
    try {
      return eventBuffer.poll(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.interrupted(); // Reset flag
      return null;
    }
  }

  public void put(Event evt) {
    try {
      eventBuffer.put(evt);
    } catch (InterruptedException e) {
      Thread.interrupted(); // Reset flag
    }
  }
}
