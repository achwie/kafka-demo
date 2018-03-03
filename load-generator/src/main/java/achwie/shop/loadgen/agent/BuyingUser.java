package achwie.shop.loadgen.agent;

import java.util.List;
import java.util.Random;

import org.apache.http.client.HttpClient;

import achwie.shop.loadgen.command.AddToCartCommand;
import achwie.shop.loadgen.command.GetCatalogCommand;
import achwie.shop.loadgen.command.LoginCommand;
import achwie.shop.loadgen.command.LogoutCommand;
import achwie.shop.loadgen.command.PlaceOrderCommand;
import achwie.shop.loadgen.command.ViewOrdersCommand;
import achwie.shop.loadgen.entities.Catalog;
import achwie.shop.loadgen.entities.CatalogItem;
import achwie.shop.loadgen.entities.LoginCredentials;

/**
 * 
 * @author 22.02.2016, Achim Wiedemann
 */
public class BuyingUser implements Agent {
  private final int maxItemsToBuy = 3;
  private final String frontendBaseUrl;
  private final LoginCredentials userCreds;

  public BuyingUser(String frontendBaseUrl, LoginCredentials userCreds) {
    this.frontendBaseUrl = frontendBaseUrl;
    this.userCreds = userCreds;
  }

  @Override
  public void run(CallContext context) throws Exception {
    final HttpClient httpClient = context.getHttpClient();

    new LoginCommand(frontendBaseUrl, userCreds).run(httpClient);

    final Catalog catalog = new GetCatalogCommand(frontendBaseUrl).run(httpClient);
    putRandomItemsIntoCart(catalog, httpClient);

    new PlaceOrderCommand(frontendBaseUrl).run(httpClient);

    new ViewOrdersCommand(frontendBaseUrl).run(httpClient);
    new LogoutCommand(frontendBaseUrl).run(httpClient);
  }

  private void putRandomItemsIntoCart(Catalog catalog, HttpClient httpClient) throws Exception {
    final List<CatalogItem> catalogItems = catalog.getItems();
    final Random rand = new Random();
    for (int i = 0; i < rand.nextInt(maxItemsToBuy) + 1; i++) {
      final CatalogItem item = catalogItems.get(rand.nextInt(catalogItems.size()));
      new AddToCartCommand(frontendBaseUrl, item.getId(), 1).run(httpClient);
    }
  }
}