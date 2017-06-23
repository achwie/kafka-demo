package achwie.hystrixdemo.auth;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import achwie.hystrixdemo.util.security.SecurityScope;
import achwie.hystrixdemo.util.security.http.SecurityScopeUtils;

/**
 * 
 * @author 21.11.2015, Achim Wiedemann
 */
public class User {
  public static final User ANONYMOUS = new User(null, "Anonymous", SecurityScope.VISITOR.getName());
  private final String userId;
  private final String userName;
  private final Set<SecurityScope> scopes;

  @JsonCreator
  User(@JsonProperty("id") String userId, @JsonProperty("userName") String userName, @JsonProperty("scopes") String... scopeNames) {
    this.userId = userId;
    this.userName = userName;
    this.scopes = SecurityScopeUtils.parseScopes(scopeNames);
  }

  public String getId() {
    return userId;
  }

  public boolean isLoggedIn() {
    return this != ANONYMOUS;
  }

  public String getUserName() {
    return userName;
  }

  public Set<SecurityScope> getScopes() {
    return scopes;
  }
}
