package achwie.shop.order.read;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 21.11.2015, Achim Wiedemann
 */
public class OrderItemDto {
  private final String productId;
  private final String productName;
  private final int quantity;

  @JsonCreator
  public OrderItemDto(@JsonProperty("productId") String productId, @JsonProperty("productName") String productName, @JsonProperty("quantity") int quantity) {
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

  @Override
  public String toString() {
    return String.format("%s[productId: %s, productName: %s, quantity: %d]", getClass().getSimpleName(), productId, productName, quantity);
  }
}
