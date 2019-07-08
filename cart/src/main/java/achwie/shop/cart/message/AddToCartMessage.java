package achwie.shop.cart.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Kafka DTO for adding an item to the cart.
 * 
 * @author 08.07.2019, Achim Wiedemann
 */
public final class AddToCartMessage {
  public final String cartId;
  public final String productName;
  public final String productId;
  public final int quantity;

  @JsonCreator
  public AddToCartMessage(@JsonProperty("cartId") String cartId, @JsonProperty("productName") String productName, @JsonProperty("productId") String productId,
      @JsonProperty("quantity") int quantity) {
    this.cartId = cartId;
    this.productName = productName;
    this.productId = productId;
    this.quantity = quantity;
  }
}