package achwie.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import achwie.shop.eventstore.EventStore;
import achwie.shop.eventstore.inmemory.InMemoryEventStore;

/**
 * Starts the order service with Apache Kafka as the event transport.
 * 
 * @author 02.01.2016, Achim Wiedemann
 */
@SpringBootApplication
public class OrderStarter {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(OrderStarter.class, args);
  }

  @Bean
  public EventStore eventStore() {
    return new InMemoryEventStore();
  }
}
