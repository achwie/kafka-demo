package achwie.shop.loadgen.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import achwie.shop.loadgen.entities.CartItem;
import achwie.shop.util.ServicesConfig;

/**
 * 
 * @author 12.01.2016, Achim Wiedemann
 *
 */
public class AddToCartCommand implements HttpClientCommand<Void> {
  private final String addToCartUrl;
  private final String productId;
  private final int quantity;

  public AddToCartCommand(String frontendBaseUrl, String productId, int quantity) {
    this.addToCartUrl = frontendBaseUrl + "/add-to-cart";
    this.productId = productId;
    this.quantity = quantity;
  }

  @Override
  public Void run(HttpClient httpClient) throws Exception {
    final List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("productId", productId));
    params.add(new BasicNameValuePair("quantity", String.valueOf(quantity)));

    final HttpPost httpPost = new HttpPost(addToCartUrl);
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    final HttpResponse response = httpClient.execute(httpPost);
    // Even if we don't care for the content: make sure resources are released!
    // We consume the HTTP entity in contrast to simply closing the response,
    // because this way HttpClient will try to re-use the connection, whereas
    // when closing the response the connection will be discarded. When doing
    // lots of fast subsequent requests this prevents the OS from using up
    // all it's client-sockets, which will manifest itself in a
    // SocketException. This is subtle, but it's mentioned in the docs at
    // https://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html
    EntityUtils.consume(response.getEntity());

    return null;
  }

  public static void main(String[] args) throws Exception {
    try (CloseableHttpClient httpClient = HttpClientFactory.createHttpClient()) {
      final ServicesConfig servicesConfig = new ServicesConfig(ServicesConfig.FILENAME_SERVICES_PROPERTIES);
      final String frontendBaseUrl = servicesConfig.getProperty(ServicesConfig.PROP_FRONTEND_BASEURL);

      final CartItem itemToAdd = new CartItem("A123", "Something awesome", 1);

      new AddToCartCommand(frontendBaseUrl, itemToAdd.getProductId(), 1).run(httpClient);
    }
  }
}
