package achwie.shop.stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import achwie.shop.util.latency.LatencySimulator;

/**
 * 
 * @author 11.11.2015, Achim Wiedemann
 */
@Component
public class StockService {
  private final StockRepository stockRepo;
  private final LatencySimulator latencySimulator;

  @Autowired
  public StockService(StockRepository stockRepo, LatencySimulator latencySimulator) {
    this.stockRepo = stockRepo;
    this.latencySimulator = latencySimulator;
  }

  /**
   * Returns the stock quantity for a given product.
   * 
   * @param productId The product ID.
   * @return The quantity for the given product or {@code -1} if there was no
   *         according stock count entry.
   */
  public int getStockQuantity(String productId) {
    latencySimulator.beLatent();

    final int[] quantities = stockRepo.getQuantities(Arrays.asList(productId));

    return quantities[0];
  }

  /**
   * Checks if all passed products are available in the requested quantities and
   * places a hold on them if, and only if, all of them are available. The
   * indices of the {@code productIds} array have to correspond to the indices
   * of the {@code quantities} array.
   * 
   * @param productIds The products to put a hold on.
   * @param quantities The quantities of the products to put a hold on.
   * @return A list of product IDs on which <strong>no hold</strong> could be
   *         placed.
   * @throws IllegalArgumentException If one of the arrays is {@code null} or
   *           they differ in length.
   */
  public String[] putHoldOnAll(String[] productIds, int[] quantities) {
    latencySimulator.beLatent();

    if (productIds == null || quantities == null || productIds.length != quantities.length) {
      // TODO: More helpful message
      throw new IllegalArgumentException("Neither product-ids or quantities must be null and both arrays need to have the same length!");
    }

    final String[] productIdsPlacedOnHold = new String[productIds.length];
    final int[] quantitiesPlacedOnHold = new int[quantities.length];
    final List<String> failedToPlaceOnHold = new ArrayList<>();

    for (int i = 0; i < productIds.length; i++) {
      final String productId = productIds[i];
      final int quantity = quantities[i];

      final int quantityPlacedOnHold = stockRepo.placeHoldOnAvailable(productId, quantity);

      productIdsPlacedOnHold[i] = productId;
      quantitiesPlacedOnHold[i] = quantityPlacedOnHold;

      if (quantity != quantityPlacedOnHold) {
        failedToPlaceOnHold.add(productId);
      }
    }

    if (!failedToPlaceOnHold.isEmpty())
      rollbackProductHolds(productIdsPlacedOnHold, quantitiesPlacedOnHold);

    return failedToPlaceOnHold.toArray(new String[failedToPlaceOnHold.size()]);
  }

  private void rollbackProductHolds(String[] productIds, int[] quantities) {
    for (int i = 0; i < productIds.length; i++)
      stockRepo.removeHold(productIds[i], quantities[i]);
  }
}
