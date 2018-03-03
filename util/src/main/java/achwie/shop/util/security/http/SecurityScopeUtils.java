package achwie.shop.util.security.http;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import achwie.shop.util.security.SecurityScope;

/**
 * 
 * @author 23.06.2017, Achim Wiedemann
 *
 */
public class SecurityScopeUtils {
  /**
   * Deserializes a (colon separated) list of scopes into a {@link Set}.
   * 
   * @param scopeNames A (colon separated) list of scopes.
   * @return The set of scopes or an empty set if there were no known scopes.
   * @see #scopesToString(Set) Serialize a set of scopes to a string
   */
  public static Set<SecurityScope> parseScopes(String scopeNames) {
    final String[] scopeNamesArr = scopeNames.split(":");

    return parseScopes(scopeNamesArr);
  }

  /**
   * Converts an array of of scopes names into a {@link Set} of scopes.
   * 
   * @param scopeNames An array of scope names
   * @return The set of scopes or an empty set if there were no known scopes.
   * @see #scopesToString(Set) Serialize a set of scopes to a string
   */
  public static Set<SecurityScope> parseScopes(String[] scopeNames) {
    final Set<SecurityScope> scopes = EnumSet.noneOf(SecurityScope.class);

    for (String scopeName : scopeNames) {
      final SecurityScope scope = SecurityScope.fromName(scopeName);
      if (scope != null)
        scopes.add(scope);
    }

    return scopes;
  }

  /**
   * Serializes a {@link Set} of scopes into a string representation.
   * 
   * @param scopes The set of scopes to serialize.
   * @return The string representation of the given set or an emtpy string if
   *         the given set was empty.
   * @see SecurityScopeUtils#parseScopes(String) Parse a serialized list of
   *      scopes into a set of scopes
   */
  public static String scopesToString(Set<SecurityScope> scopes) {
    return scopes.stream().map(s -> s.getName()).collect(Collectors.joining(":"));
  }
}
