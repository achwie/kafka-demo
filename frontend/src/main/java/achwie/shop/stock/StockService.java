package achwie.shop.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author 11.11.2015, Achim Wiedemann
 */
@Component
public class StockService {
  private final RestTemplate restTemplate;
  private final String stockServiceBaseUrl;

  @Autowired
  public StockService(@Value("${service.stock.baseurl}") String stockServiceBaseUrl, RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.stockServiceBaseUrl = stockServiceBaseUrl;

  }

  /**
   * Returns the stock quantity for a product.
   * 
   * @param productId The product ID to get the stock quantity for.
   * @return The stock quantity ({@code >= 0}) or {@code -1} if there was no
   *         stock count entry for the given product ID.
   */
  public int getStockQuantity(String productId) {
    return new GetStockQuantityCommand(restTemplate, stockServiceBaseUrl, productId).execute();
  }
}