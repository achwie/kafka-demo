package achwie.shop.util.security;

import java.util.Collections;
import java.util.Set;

/**
 * <p>
 * Holds security related information about the <em>current request</em>.
 * </p>
 * <p>
 * <strong>Thread safety:</strong> Although this object is immutable and
 * technically safe to pass on to other threads, note the security information
 * it stores relates only to the current request thread and is short lived.
 * </p>
 * 
 * @author 16.06.2017, Achim Wiedemann
 */
public class SecurityContext {
  private final String user;
  private final Set<SecurityScope> scopes;

  public SecurityContext(String user, Set<SecurityScope> scopes) {
    this.user = user;
    this.scopes = scopes;
  }

  /**
   * Returns the scopes that the current user has.
   * 
   * @return The scopes that the current user has - will return an empty
   *         {@link Set} if n/a.
   */
  public Set<SecurityScope> getScopes() {
    return Collections.unmodifiableSet(scopes);
  }

  /**
   * Returns whether this security context contains the given scope.
   * 
   * @param scopeToCheck The scope to check.
   * @return Whether this security context contains a scope with the given scope
   *         name.
   */
  public boolean containsScope(SecurityScope scopeToCheck) {

    for (SecurityScope scope : scopes)
      if (scope.containsScope(scopeToCheck))
        return true;

    return false;
  }

  /**
   * Returns the user that made the current request.
   * 
   * @return The user that made the current request - may be {@code null}.
   */
  public String getUser() {
    return user;
  }
}