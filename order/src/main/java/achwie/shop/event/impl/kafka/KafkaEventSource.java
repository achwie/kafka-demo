package achwie.shop.event.impl.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import achwie.shop.event.api.Event;
import achwie.shop.event.api.EventSource;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public class KafkaEventSource implements EventSource {
  private static final int BUFFER_CAPACITY = 1024;
  private final BlockingQueue<Event> eventBuffer;
  private final KafkaConsumerPoller poller;

  public KafkaEventSource(Properties props, String topicName, KafkaEventFactory eventFactory) {
    eventBuffer = new ArrayBlockingQueue<>(BUFFER_CAPACITY);

    final String pollerThreadName = "Kafka poller thread [topic: " + topicName + "]";
    new Thread(poller = new KafkaConsumerPoller(props, topicName, eventFactory, eventBuffer), pollerThreadName).start();
  }

  @Override
  public Event read(int timeoutMillis) {
    try {
      return eventBuffer.poll(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      System.err.println("JsonEvent source polling has been interrupted! Shutting down Kafka poller thread... (details: " + e.getMessage() + ")");
      Thread.interrupted(); // Reset flag
      poller.stop();
      return null;
    }
  }

  private static class KafkaConsumerPoller implements Runnable {
    private static final int POLL_TIME_MILLIS = 1000;
    private final Properties props;
    private final String topicName;
    private final KafkaEventFactory eventFactory;
    private final BlockingQueue<Event> eventBuffer;
    private volatile boolean running = true;

    public KafkaConsumerPoller(Properties props, String topicName, KafkaEventFactory eventFactory, BlockingQueue<Event> eventBuffer) {
      this.props = props;
      this.topicName = topicName;
      this.eventFactory = eventFactory;
      this.eventBuffer = eventBuffer;
    }

    @Override
    public void run() {
      try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
        consumer.subscribe(Arrays.asList(topicName));
        while (running) {
          ConsumerRecords<String, String> records = consumer.poll(POLL_TIME_MILLIS);
          for (ConsumerRecord<String, String> record : records) {
            try {
              final String value = record.value();

              final Event event = eventFactory.deserialize(value);

              if (event != null)
                eventBuffer.put(event);

            } catch (InterruptedException e) {
              System.err.println(
                  "Waiting to put element into full event buffer has been interrupted! Shutting down Kafka poller thread... (details: " + e.getMessage()
                      + ")");
              Thread.interrupted(); // Reset flag
              running = false;
              return;
            }
          }
        }
      }
    }

    public void stop() {
      running = false;
    }
  }
}
