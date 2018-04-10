package achwie.shop.order;

import org.springframework.web.client.RestOperations;

/**
 * 
 * @author 09.04.2018, Achim Wiedemann
 *
 */
public class GetProductDetailsCommand extends HystrixRestCommand<ProductDetails> {
  private final String url;

  public GetProductDetailsCommand(RestOperations restOps, String catalogServiceBaseUrl, String productId) {
    super(CommandGroup.GET_PRODUCT_DETAILS, restOps);
    this.url = catalogServiceBaseUrl + "/" + productId;
  }

  @Override
  protected ProductDetails run() throws Exception {
    final ProductDetails productDetails = restOps.getForObject(url, ProductDetails.class);
    return productDetails;
  }

  @Override
  protected ProductDetails getFallback() {
    return null;
  }
}
