package achwie.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import achwie.shop.event.api.EventSource;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventProcessor;
import achwie.shop.event.impl.EventWrapper;
import achwie.shop.eventstore.DomainEvent;
import achwie.shop.eventstore.EventStore;
import achwie.shop.eventstore.EventStoreListener;
import achwie.shop.eventstore.inmemory.InMemoryEventStore;
import achwie.shop.order.write.OrderEventPublisher;

/**
 * 
 * @author 05.04.2018, Achim Wiedemann
 *
 */
public abstract class AbstractOrderStarter {
  @Bean
  @Autowired
  public EventStore createEventStore(OrderEventPublisher orderEventPublisher) {
    final InMemoryEventStore eventStore = new InMemoryEventStore();

    // Automatically publish all events after they got saved to the event store
    eventStore.addListener(new EventStoreListener() {
      @Override
      public void onSave(List<DomainEvent> events) {
      }

      @Override
      public void onSaved(List<DomainEvent> events) {
        for (DomainEvent event : events)
          orderEventPublisher.publish(event);
      }
    });

    return eventStore;
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
