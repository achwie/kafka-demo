package achwie.shop.order.write.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.write.event.FailedToPutHoldOnProducts;

/**
 * 
 * @author 13.04.2018, Achim Wiedemann
 *
 */
public class FailedToPutHoldOnProductsHandler implements EventHandler<FailedToPutHoldOnProducts> {
  private static final Logger LOG = LoggerFactory.getLogger(FailedToPutHoldOnProductsHandler.class);

  @Override
  public void onEvent(FailedToPutHoldOnProducts event) {
    // TODO: Notify customer that his order will be delayed
    LOG.info("Failed to put hold on products: {}", event.getProductIdsNotInStock());
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_FAILED_TO_PUT_HOLD_ON_PRODUCTS_1_0;
  }

}
