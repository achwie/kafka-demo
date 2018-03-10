package achwie.shop.order.event;

import static achwie.shop.order.event.EventType.TYPE_ORDER;
import static achwie.shop.order.event.EventVersion.VERSION_ORDER_1_0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.event.api.Event;
import achwie.shop.event.impl.kafka.KafkaEventFactory;
import achwie.shop.order.Order;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
@Component
public class EventFactory {
  private final KafkaEventFactory kafkaEventFactory;

  @Autowired
  public EventFactory(KafkaEventFactory kafkaEventFactory) {
    this.kafkaEventFactory = kafkaEventFactory;
  }

  public Event createOrderEvent(Order order) {
    return kafkaEventFactory.createEvent(VERSION_ORDER_1_0, TYPE_ORDER, order);
  }
}
