package achwie.shop.order.write.domain;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public class MutableProductDetails {
  private String productName;

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  @Override
  public String toString() {
    return String.format("%s[productName: %s]", getClass().getSimpleName(), productName);
  }
}
