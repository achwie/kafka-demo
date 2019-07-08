package achwie.shop.cart.message;

import achwie.shop.cart.CartItem;

/**
 * 
 * @author 08.07.2019, Achim Wiedemann
 *
 */
public class AddToCartMessage {
  private final String cartId;
  private final CartItem cartItem;

  public AddToCartMessage(String cartId, CartItem cartItem) {
    this.cartId = cartId;
    this.cartItem = cartItem;
  }

  public String getCartId() {
    return cartId;
  }

  public String getProductId() {
    return cartItem.getProductId();
  }

  public String getProductName() {
    return cartItem.getProductName();
  }

  public int getQuantity() {
    return cartItem.getQuantity();
  }
}
