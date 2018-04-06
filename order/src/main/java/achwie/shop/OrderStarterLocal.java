package achwie.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import achwie.shop.event.api.EventSink;
import achwie.shop.event.impl.local.LocalEventSink;
import achwie.shop.event.impl.local.LocalEventSource;

/**
 * Starts the order service using the local (in-memory) event transport.
 * 
 * @author 10.03.2018, Achim Wiedemann
 */
@SpringBootApplication
public class OrderStarterLocal extends AbstractOrderStarter {

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
}
