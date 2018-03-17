package achwie.shop.order.store.write;

import achwie.shop.order.store.read.OrderItem;
import achwie.shop.order.store.read.ProductDetails;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class MutableOrderItem implements OrderItem {
  private String productId;
  private int quantity;
  private ProductDetails productDetails;

  public MutableOrderItem(String productId, int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getProductName() {
    return productDetails != null ? productDetails.getProductName() : null;
  }

  public void setProductDetails(ProductDetails productDetails) {
    this.productDetails = productDetails;
  }
}
