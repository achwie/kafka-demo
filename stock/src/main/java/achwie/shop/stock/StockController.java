package achwie.shop.stock;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author 30.11.2015, Achim Wiedemann
 *
 */
@KafkaListener(topics = "${kafka.topic.order}")
@RestController
@RequestMapping("/stock")
public class StockController {
  private static final Logger LOG = LoggerFactory.getLogger(StockController.class);
  private final StockService stockService;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final String kafkaOrderTopic;

  @Autowired
  public StockController(StockService stockService, KafkaTemplate<String, Object> kafkaTemplate, @Value("${kafka.topic.order}") String kafkaOrderTopic) {
    this.stockService = stockService;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaOrderTopic = kafkaOrderTopic;
  }

  @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
  public ResponseEntity<Integer> getQuantity(@PathVariable String productId) {
    final int quantity = stockService.getStockQuantity(productId);

    final HttpStatus status = (quantity != -1) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    return new ResponseEntity<Integer>(quantity, status);
  }

  @KafkaHandler
  public void onPutHoldOnOrder(PutProductsOnHoldRequest putHoldOnOrder) {
    final var failedProductIds = stockService.putHoldOnAll(putHoldOnOrder.getProductIds(), putHoldOnOrder.getQuantities());

    if (failedProductIds.length == 0) {
      kafkaTemplate.send(kafkaOrderTopic, new OrderConfirmedEvent(putHoldOnOrder.getOrderId()));
    } else {
      // TODO: Handle error more thoroughly (e.g. send message)
      LOG.error("Put hold on order (failed pids: {})!", Arrays.asList(failedProductIds));
    }
  }

  @KafkaHandler(isDefault = true)
  public void onUnknown(Object obj) {
    // Ignore all unknown messages
    LOG.debug("Received unknown message of type {} -> {}", obj.getClass(), obj);
  }

  @RequestMapping(value = "/put-hold-on-all", method = RequestMethod.POST)
  public ResponseEntity<String[]> putHoldOnAll(@RequestBody PutProductsOnHoldRequest holdRequest) {
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
}
