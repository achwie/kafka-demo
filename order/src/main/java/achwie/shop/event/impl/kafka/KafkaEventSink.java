package achwie.shop.event.impl.kafka;

import java.util.Objects;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventHeader;
import achwie.shop.event.api.EventSink;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class KafkaEventSink implements EventSink {
  private final ObjectMapper objectMapper;
  private final KafkaProducer<String, String> kafkaProducer;
  private final String topicName;

  public KafkaEventSink(KafkaProducer<String, String> kafkaProducer, String topicName, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.kafkaProducer = kafkaProducer;
    this.topicName = topicName;
  }

  @Override
  public void publish(Event evt) {
    Objects.requireNonNull(evt, "Given event must not be null!");

    final EventHeader header = evt.getHeader();
    try {
      final String key = Integer.toString(header.getType());
      final String payloadJson = objectMapper.writeValueAsString(evt);

      kafkaProducer.send(new ProducerRecord<String, String>(topicName, key, payloadJson));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(String.format("Could not serialize payload! (event-type: %d, event-version: %d)", header.getType(), header.getVersion()), e);
    }
  }
}
