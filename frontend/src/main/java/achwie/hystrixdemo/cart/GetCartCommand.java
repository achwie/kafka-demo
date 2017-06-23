package achwie.hystrixdemo.cart;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import achwie.hystrixdemo.CommandGroup;
import achwie.hystrixdemo.HystrixRestCommand;
import achwie.hystrixdemo.auth.User;
import achwie.hystrixdemo.util.security.SecurityHeader;
import achwie.hystrixdemo.util.security.http.SecurityScopeUtils;

/**
 * 
 * @author 30.01.2016, Achim Wiedemann
 */
class GetCartCommand extends HystrixRestCommand<Cart> {
  private final String url;
  private final User user;

  protected GetCartCommand(RestOperations restOps, String cartServiceBaseUrl, String cartId, User user) {
    super(CommandGroup.CART_SERVICE, restOps);
    this.url = cartServiceBaseUrl + "/" + cartId;
    this.user = user;
  }

  @Override
  protected Cart run() throws Exception {
    try {
      // TODO: Take care of security headers in a more global manner
      final HttpHeaders headers = new HttpHeaders();
      headers.add(SecurityHeader.USER.getHeaderName(), user.getUserName());
      // TODO: Get real authenticated user data
      headers.add(SecurityHeader.SCOPES.getHeaderName(), SecurityScopeUtils.scopesToString(user.getScopes()));
      final URI uri = new URI(url);

      final RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET, uri);

      final ResponseEntity<Cart> responseEntity = restOps.exchange(requestEntity, Cart.class);
      return responseEntity.getBody();

      // return restOps.getForObject(url, Cart.class);
    } catch (HttpStatusCodeException e) {
      // Returns 404 for non-existent cart - otherwise we have an error
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return getFallback(); // Don't trip circuit breaker
      } else {
        LOG.error("Unexpected response while getting cart at {} (status: {}, response body: '{}')", url, e.getStatusCode(), e.getResponseBodyAsString());
        throw e;
      }
    }
  }

  @Override
  protected Cart getFallback() {
    return Cart.EMPTY_CART;
  }

  @Override
  protected String getCacheKey() {
    return url;
  }
}
