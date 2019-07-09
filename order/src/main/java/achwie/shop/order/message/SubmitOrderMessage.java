package achwie.shop.order.message;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.shop.order.write.CartItem;

/**
 * 
 * @author 08.07.2019, Achim Wiedemann
 */
public class SubmitOrderMessage {
  private final String sessionId;
  private final List<CartItem> cartItems;

  @JsonCreator
  public SubmitOrderMessage(@JsonProperty("sessionId") String sessionId, @JsonProperty("items") List<CartItem> cartItems) {
    this.sessionId = sessionId;
    this.cartItems = (cartItems != null) ? cartItems : Collections.emptyList();
  }

  public String getSessionId() {
    return sessionId;
  }

  public List<CartItem> getItems() {
    return Collections.unmodifiableList(cartItems);
  }

  public boolean isEmpty() {
    return cartItems.isEmpty();
  }

  @Override
  public String toString() {
    return String.format("%s[cartItems: %s]", getClass().getSimpleName(), cartItems);
  }
}
