package achwie.shop.cart;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import achwie.shop.util.security.SecurityContext;
import achwie.shop.util.security.SecurityContextProvider;
import achwie.shop.util.security.SecurityScope;

/**
 * 
 * @author 11.11.2015, Achim Wiedemann
 */
@RestController
@RequestMapping("/cart")
public class CartController {
  private final CartService cartService;
  private final SecurityContextProvider secContextProvider;

  @Autowired
  public CartController(CartService cartService, SecurityContextProvider secContextProvider) {
    this.cartService = cartService;
    this.secContextProvider = secContextProvider;
  }

  @RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
  public ResponseEntity<Cart> viewCart(@PathVariable String cartId) {
    final SecurityContext securityContext = secContextProvider.getCurrentContext();
    final String user = securityContext.getUser();
    final Set<SecurityScope> scopes = securityContext.getScopes();

    // TODO: Do something with the user information (cart is probably not the
    // best example, since also anonymous users can use it)
    System.out.println(String.format("User: %s, Scopes: %s", user, scopes));
    final Cart cart = cartService.getCart(cartId);

    final HttpStatus code = (cart != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;

    return new ResponseEntity<Cart>(cart, code);
  }

  @RequestMapping(value = "/{cartId}", method = RequestMethod.POST)
  public ResponseEntity<String> addToCart(@PathVariable String cartId, @RequestBody CartItem cartItem) {
    if (cartItem != null && cartItem.getProductId() != null) {
      cartService.addToCart(cartId, cartItem.getProductId(), cartItem.getProductName(), cartItem.getQuantity());
      return new ResponseEntity<String>("OK", HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Invalid request!", HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
  public void clearCart(@PathVariable String cartId) {
    cartService.clearCart(cartId);
  }

}
