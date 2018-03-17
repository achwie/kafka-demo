package achwie.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import achwie.shop.event.api.EventSink;
import achwie.shop.event.api.EventSource;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventProcessor;
import achwie.shop.event.impl.EventWrapper;
import achwie.shop.event.impl.local.LocalEventSink;
import achwie.shop.event.impl.local.LocalEventSource;

/**
 * Starts the order service using the local (in-memory) event transport.
 * 
 * @author 10.03.2018, Achim Wiedemann
 */
@SpringBootApplication
public class OrderStarterLocal {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(OrderStarterLocal.class, args);
  }

  @Bean
  @Autowired
  public EventSink createEventSink(LocalEventSource localEventSource) {
    return new LocalEventSink(localEventSource);
  }

  @Bean
  @Autowired
  public LocalEventSource createEventSource() {
    return new LocalEventSource();
  }

  @Bean
  @Autowired
  public EventHandlerChain createEventHandlerChain() {
    return new EventHandlerChain();
  }

  @Bean
  @Autowired
  public EventProcessor createAndStartEventProcessor(EventSource eventSource, EventHandlerChain handlerChain, EventWrapper eventWrapper) {
    final EventProcessor eventProcessor = new EventProcessor(eventSource, handlerChain, eventWrapper);

    new Thread(eventProcessor, "Event processor").start();

    return eventProcessor;
  }
}
