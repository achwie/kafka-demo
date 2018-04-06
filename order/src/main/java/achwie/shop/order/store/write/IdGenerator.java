package achwie.shop.order.store.write;

import org.springframework.stereotype.Component;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
@Component
public class IdGenerator {
  private int currentId = 1;

  public synchronized String nextOrderId() {
    return "shop://order/" + Integer.toString(currentId++);
  }
}
