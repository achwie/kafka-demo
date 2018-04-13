package achwie.shop.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.util.latency.LatencySimulator;

/**
 * 
 * @author 30.11.2015, Achim Wiedemann
 *
 */
@RestController
@RequestMapping("/stock")
public class StockController {
  private final StockService stockService;

  @Autowired
  public StockController(StockService stockService) {
    this.stockService = stockService;
  }

  @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
  public ResponseEntity<Integer> getQuantity(@PathVariable String productId) {
    LatencySimulator.beLatent();

    final int quantity = stockService.getStockQuantity(productId);

    final HttpStatus status = (quantity != -1) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    return new ResponseEntity<Integer>(quantity, status);
  }

  @RequestMapping(value = "/put-hold-on-all", method = RequestMethod.POST)
  public ResponseEntity<String[]> putHoldOnAll(@RequestBody PutProductsOnHoldRequest holdRequest) {
    LatencySimulator.beLatent();

    HttpStatus code = HttpStatus.OK;
    String[] failedProductIds;
    try {
      failedProductIds = stockService.putHoldOnAll(holdRequest.getProductIds(), holdRequest.getQuantities());

      if (failedProductIds.length > 0) {
        code = HttpStatus.CONFLICT;
      }
    } catch (IllegalArgumentException e) {
      failedProductIds = new String[0];
      code = HttpStatus.BAD_REQUEST;
    }

    return new ResponseEntity<>(failedProductIds, code);
  }

  // ---------------------------------------------------------------------------
  public static class PutProductsOnHoldRequest {
    private String[] productIds;
    private int[] quantities;

    public String[] getProductIds() {
      return productIds;
    }

    public void setProductIds(String[] productsIds) {
      this.productIds = productsIds;
    }

    public int[] getQuantities() {
      return quantities;
    }

    public void setQuantities(int[] quantities) {
      this.quantities = quantities;
    }
  }
}
