package achwie.shop.cart.message;

/**
 * 
 * @author 08.07.2019, Achim Wiedemann
 *
 */
public class ClearCartMessage {
  private final String cartId;

  public ClearCartMessage(String cartId) {
    this.cartId = cartId;
  }

  public String getCartId() {
    return cartId;
  }
}
