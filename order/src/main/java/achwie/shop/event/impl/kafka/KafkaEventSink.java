package achwie.shop.event.impl.kafka;

import java.util.Objects;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;
import achwie.shop.event.api.EventSink;
import achwie.shop.event.impl.EventSerializer;
import achwie.shop.event.impl.SerializationException;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class KafkaEventSink implements EventSink {
  private final KafkaProducer<String, String> kafkaProducer;
  private final String topicName;
  private final EventSerializer eventSerializer;

  public KafkaEventSink(KafkaProducer<String, String> kafkaProducer, String topicName, EventSerializer eventSerializer) {
    this.kafkaProducer = kafkaProducer;
    this.topicName = topicName;
    this.eventSerializer = eventSerializer;
  }

  @Override
  public boolean publish(Event evt) {
    Objects.requireNonNull(evt, "Given event must not be null!");

    try {
      final EventHeader header = evt.getHeader();
      final String key = Integer.toString(header.getType());
      final String payloadJson = eventSerializer.serialize(evt);

      kafkaProducer.send(new ProducerRecord<String, String>(topicName, key, payloadJson));
      return true;
    } catch (SerializationException e) {
      // TODO: Better handling
      e.printStackTrace();
    }
    return false;
  }
}
