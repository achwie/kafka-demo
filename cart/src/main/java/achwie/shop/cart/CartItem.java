package achwie.shop.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 11.11.2015, Achim Wiedemann
 */
public class CartItem {
  private final String productId;
  private final String productName;
  private int quantity;

  @JsonCreator
  public CartItem(@JsonProperty("productId") String productId, @JsonProperty("productName") String productName, @JsonProperty("quantity") int quantity) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
  }

  public String getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void decreaseQuantity(int by) {
    quantity = Math.max(0, quantity - by); // make sure to be > 0
  }

  public void increaseQuantity(int by) {
    quantity = Math.max(0, quantity + by); // make sure to be > 0
  }
}
