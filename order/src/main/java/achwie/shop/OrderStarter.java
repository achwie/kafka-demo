package achwie.shop;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import achwie.shop.event.api.EventSink;
import achwie.shop.event.api.EventSource;
import achwie.shop.event.impl.kafka.KafkaEventFactory;
import achwie.shop.event.impl.kafka.KafkaEventSink;
import achwie.shop.event.impl.kafka.KafkaEventSource;
import achwie.shop.order.eventhandler.EventHandlerChain;

/**
 * 
 * @author 02.01.2016, Achim Wiedemann
 */
@SpringBootApplication
public class OrderStarter {
  private static final String BOOTSTRAP_SERVERS = "192.168.56.2:9092,192.168.56.2:9093,192.168.56.2:9094";

  public static void main(String[] args) throws Exception {
    SpringApplication.run(OrderStarter.class, args);
  }

  @Bean
  @Autowired
  public EventSink createKafkaEventSink(ObjectMapper objectMapper, @Value("${kafka.topicname.orders}") String topicName) {
    // TODO: Properties should be passed in from the outside
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "test-producer");
    // props.put("acks", "all");
    // props.put("retries", 0);
    // props.put("batch.size", 16384);
    // props.put("linger.ms", 1);
    // props.put("buffer.memory", 33554432);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);

    return new KafkaEventSink(kafkaProducer, topicName, objectMapper);
  }

  @Bean
  @Autowired
  public EventSource createKafkaEventSource(@Value("${kafka.topicname.orders}") String topicName, KafkaEventFactory eventFactory) {
    // TODO: Properties should be passed in from the outside
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    // props.put("enable.auto.commit", "true");
    // props.put("auto.commit.interval.ms", "1000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new KafkaEventSource(props, topicName, eventFactory);
  }

  @Bean
  @Autowired
  public KafkaEventFactory createKafkaEventFactory(ObjectMapper objectMapper, EventHandlerChain eventHandlerChain) {
    return new KafkaEventFactory(objectMapper, eventHandlerChain);
  }
}
