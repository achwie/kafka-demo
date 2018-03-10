package achwie.shop.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 21.11.2015, Achim Wiedemann
 */
public class OrderItem {
  private final String productId;
  private final String productName;
  private final int quantity;

  // TODO: Don't want serialization info in my domain classes
  @JsonCreator
  public OrderItem(@JsonProperty("productId") String productId, @JsonProperty("productName") String productName, @JsonProperty("quantity") int quantity) {
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
}
