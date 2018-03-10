package achwie.shop.order.eventhandler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.impl.kafka.PayloadTypeMapper;
import achwie.shop.order.event.EventHandler;
import achwie.shop.order.read.InMemoryOrderRepository;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
@Component
public class EventHandlerChain implements PayloadTypeMapper {
  private final InMemoryOrderRepository orderRepo;
  private final List<EventHandler<?>> handlers = new ArrayList<>();

  @Autowired
  public EventHandlerChain(InMemoryOrderRepository orderRepo) {
    this.orderRepo = orderRepo;

    initHandlers();
  }

  private void initHandlers() {
    handlers.add(new OrderEventHandler(orderRepo));
  }

  public EventHandler<?> findEventHandlerFor(int eventType, int eventVersion) {
    for (EventHandler<?> handler : handlers)
      if (handler.canHandle(eventType, eventVersion))
        return handler;

    return null;
  }

  @Override
  public Class<?> getPayloadTypeFor(int eventType, int eventVersion) {
    final EventHandler<?> eventHandler = findEventHandlerFor(eventType, eventVersion);

    return (eventHandler != null) ? eventHandler.getPayloadType() : null;
  }
}
