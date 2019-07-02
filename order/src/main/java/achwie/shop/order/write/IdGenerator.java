package achwie.shop.order.write;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

/**
 * <strong>Thread safety:</strong> This class is thread safe.
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class IdGenerator {
  private AtomicInteger currentId = new AtomicInteger(0);

  public String nextOrderId() {
    final int currentOrderId = currentId.getAndIncrement();
    return "shop://order/" + Integer.toString(currentOrderId);
  }
}
