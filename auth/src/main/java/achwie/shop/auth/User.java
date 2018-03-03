package achwie.shop.auth;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import achwie.shop.util.security.SecurityScope;

/**
 * 
 * @author 21.11.2015, Achim Wiedemann
 */
public class User {
  public static final User ANONYMOUS = new User(null, "Anonymous", EnumSet.of(SecurityScope.VISITOR));
  private final String userId;
  private final String userName;
  private final Set<SecurityScope> scopes;

  public User(String userId, String userName, Set<SecurityScope> scopes) {
    this.userId = userId;
    this.userName = userName;
    this.scopes = scopes;
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
    return Collections.unmodifiableSet(scopes);
  }
}
