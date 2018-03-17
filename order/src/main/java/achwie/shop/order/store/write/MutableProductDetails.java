package achwie.shop.order.store.write;

import achwie.shop.order.store.read.ProductDetails;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class MutableProductDetails implements ProductDetails {
  private String productName;

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }
}
