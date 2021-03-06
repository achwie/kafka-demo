package achwie.shop.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import achwie.shop.auth.SessionService;
import achwie.shop.auth.User;
import achwie.shop.cart.Cart;
import achwie.shop.cart.CartService;
import achwie.shop.stock.StockService;

/**
 * 
 * @author 10.11.2015, Achim Wiedemann
 */
@Controller
public class CatalogController {
  private final CatalogService catalogService;
  private final CartService cartService;
  private final StockService stockService;
  private final SessionService sessionService;

  @Autowired
  public CatalogController(CatalogService catalogService, CartService cartService, StockService stockService, SessionService sessionService) {
    this.catalogService = catalogService;
    this.cartService = cartService;
    this.stockService = stockService;
    this.sessionService = sessionService;
  }

  @RequestMapping("/")
  public String entryPage(Model model, HttpServletRequest req) {
    return "redirect:catalog";
  }

  @RequestMapping(value = "/catalog", method = RequestMethod.GET)
  public String viewCatalog(Model model, HttpServletRequest req) {
    final User user = sessionService.getSessionUser();
    final String sessionId = sessionService.getSessionId();
    final Cart cart = cartService.getCart(sessionId);

    final List<CatalogItem> catalogItems = catalogService.findAll();

    model.addAttribute("catalogItems", toCatalogItems(catalogItems));
    model.addAttribute("cart", cart);
    model.addAttribute("user", user);

    return "catalog";
  }

  private List<Product> toCatalogItems(List<CatalogItem> catalogItems) {
    final List<Product> products = new ArrayList<>();
    // TODO: Fetch stock quantities in bulk for better performance
    for (CatalogItem item : catalogItems) {
      final int productStockQuantity = stockService.getStockQuantity(item.getId());

      final Product product = new Product();
      product.setId(item.getId());
      product.setName(item.getName());
      product.setStockQuantity(productStockQuantity);

      products.add(product);
    }

    return products;
  }
}
