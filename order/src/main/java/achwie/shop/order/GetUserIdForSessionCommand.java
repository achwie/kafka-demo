package achwie.shop.order;

import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 01.02.2016, Achim Wiedemann
 *
 */
class GetUserIdForSessionCommand extends HystrixRestCommand<String> {
  private final String url;

  protected GetUserIdForSessionCommand(RestOperations restOps, String authServiceBaseUrl, String sessionId) {
    super(CommandGroup.AUTH_GET_USERID_FOR_SESSION, restOps);
    this.url = authServiceBaseUrl + "/" + sessionId;
  }

  @Override
  protected String run() throws Exception {
    final User user = restOps.getForObject(url, User.class);
    return user.userId;
  }

  @Override
  protected String getFallback() {
    logExecutionFailure();

    return null;
  }

  // ---------------------------------------------------------------------------
  private static final class User {
    public final String userId;

    @JsonCreator
    public User(@JsonProperty("id") String userId) {
      this.userId = userId;
    }
  }
}
