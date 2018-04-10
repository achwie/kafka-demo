package achwie.shop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author 09.04.2018, Achim Wiedemann
 *
 */
@Component
public class CatalogService {
  private final RestTemplate restTemplate = new RestTemplate();
  private final String catalogServiceBaseUrl;

  @Autowired
  public CatalogService(@Value("${service.catalog.baseurl}") String catalogServiceBaseUrl) {
    this.catalogServiceBaseUrl = catalogServiceBaseUrl;
  }

  public ProductDetails fetchProductDetails(String productId) {
    return new GetProductDetailsCommand(restTemplate, catalogServiceBaseUrl, productId).execute();
  }
}
