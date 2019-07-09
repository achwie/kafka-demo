package achwie.shop.order;

import java.util.List;

import achwie.shop.cart.Cart;
import achwie.shop.cart.CartItem;

/**
 * 
 * @author 08.07.2019, Achim Wiedemann
 *
 */
public class SubmitOrderMessage {
  private final String sessionId;
  private final Cart cart;

  public SubmitOrderMessage(String sessionId, Cart cart) {
    this.sessionId = sessionId;
    this.cart = cart;
  }

  public String getSessionId() {
    return sessionId;
  }

  public List<CartItem> getItems() {
    return cart.getItems();
  }
}
