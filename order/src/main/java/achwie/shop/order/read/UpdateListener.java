package achwie.shop.order.read;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import achwie.shop.order.message.OrderUpdatedEvent;

/**
 * 
 * @author 09.07.2019, Achim Wiedemann
 */
@Component
@KafkaListener(topics = "${kafka.topic.order}", groupId = "order-read")
public class UpdateListener {
  private final InMemoryOrderRepository orderRepo;

  public UpdateListener(InMemoryOrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

  @KafkaHandler
  public void onOrderUpdated(OrderUpdatedEvent orderUpdated) {
    orderRepo.update(orderUpdated.getUserId(), orderUpdated.getOrderId());
  }

  @KafkaHandler(isDefault = true)
  public void onUnknown(Object message) {
    // Ignore unknown messages, we're only interested in OrderUpdated events
  }
}
