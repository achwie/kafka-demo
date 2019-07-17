package achwie.shop.stock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 10.07.2019, Achim Wiedemann
 *
 */
public class PutProductsOnHoldRequest {
  private final String orderId;
  private final String[] productIds;
  private final int[] quantities;

  @JsonCreator
  public PutProductsOnHoldRequest(@JsonProperty("orderId") String orderId, @JsonProperty("productIds") String[] productIds,
      @JsonProperty("quantities") int[] quantities) {
    this.orderId = orderId;
    this.productIds = productIds;
    this.quantities = quantities;
  }

  public String getOrderId() {
    return orderId;
  }

  public String[] getProductIds() {
    return productIds;
  }

  public int[] getQuantities() {
    return quantities;
  }

  public String toString() {
    return String.format("%s[orderId: %s, productIds: %s, quantities: %s]", getClass().getSimpleName(), orderId, productIds, quantities);
  }
}