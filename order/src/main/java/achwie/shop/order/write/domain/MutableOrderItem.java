package achwie.shop.order.write.domain;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class MutableOrderItem {
  private final String productId;
  private final int quantity;
  private final String productName;

  public MutableOrderItem(String productId, int quantity, String productName) {
    this.productId = productId;
    this.quantity = quantity;
    this.productName = productName;
  }

  public String getProductId() {
    return productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getProductName() {
    return productName;
  }

  @Override
  public String toString() {
    return String.format("%s[productId: %s, quantity: %d, productName: %s]", getClass().getSimpleName(), productId, quantity, productName);
  }
}
