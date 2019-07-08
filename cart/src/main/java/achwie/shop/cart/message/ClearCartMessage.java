package achwie.shop.cart.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Kafka DTO for clearing the cart.
 * 
 * @author 08.07.2019, Achim Wiedemann
 */
public final class ClearCartMessage {
  public final String cartId;

  @JsonCreator
  public ClearCartMessage(@JsonProperty("cartId") String cartId) {
    this.cartId = cartId;
  }
}