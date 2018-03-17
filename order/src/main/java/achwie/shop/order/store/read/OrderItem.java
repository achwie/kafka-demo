package achwie.shop.order.store.read;

/**
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public interface OrderItem {

  String getProductId();

  int getQuantity();

  String getProductName();
}