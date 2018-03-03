package achwie.shop.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import achwie.shop.auth.SessionService;
import achwie.shop.auth.User;
import achwie.shop.catalog.CatalogItem;

/**
 * 
 * @author 11.11.2015, Achim Wiedemann
 */
@Component
public class CartService {
  private final RestTemplate restTemplate;
  private final String cartServiceBaseUrl;
  private final SessionService sessionService;

  @Autowired
  public CartService(@Value("${service.cart.baseurl}") String cartServiceBaseUrl, RestTemplate restTemplate, SessionService sessionService) {
    this.restTemplate = restTemplate;
    this.cartServiceBaseUrl = cartServiceBaseUrl;
    this.sessionService = sessionService;
  }

  /**
   * Adds a catalog item to a cart. If the cart did not exist before, it will be
   * created.
   * 
   * @param cartId The cart to add the item to
   * @param catalogItem The item to add to the cart
   * @param quantity How many to add
   */
  public void addToCart(String cartId, CatalogItem catalogItem, int quantity) {
    new AddToCartCommand(restTemplate, cartServiceBaseUrl, cartId, catalogItem, quantity).execute();
  }

  /**
   * Returns the contents of a certain cart.
   * 
   * @param cartId The ID of the cart to retrieve.
   * @return The cart (empty cart if there was no cart with the given ID).
   */
  public Cart getCart(String cartId) {
    final User currentUser = sessionService.getSessionUser();

    return new GetCartCommand(restTemplate, cartServiceBaseUrl, cartId, currentUser).execute();
  }

  /**
   * Clears the cart with the given ID.
   * 
   * @param cartId The cart to clear.
   */
  public void clearCart(String cartId) {
    new ClearCartCommand(restTemplate, cartServiceBaseUrl, cartId).execute();
  }
}
