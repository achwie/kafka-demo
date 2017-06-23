package achwie.hystrixdemo.util.security;

/**
 * The available security scopes for the application.
 * 
 * @author 23.06.2017, Achim Wiedemann
 */
// TODO: It would be better to move this class to a separate security module,
// since most services will have a dependency on this enum and it also is part
// of the application's domain
public enum SecurityScope {
  // Not logged in shop user
  VISITOR("visitor"),
  // Logged in shop user
  SHOPPER("shopper", VISITOR),
  // Super user with all rights
  ADMIN("admin", SHOPPER);

  private final String name;
  private final SecurityScope[] containedScopes;

  private SecurityScope(String name, SecurityScope... containedScopes) {
    this.name = name;
    this.containedScopes = containedScopes;
  }

  /**
   * Returns the scope's name(e.g. "shopper").
   * 
   * @return The scope's name(e.g. "shopper") - never {@code null}.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns whether the given scope is contained in this scope (checks for
   * nested scopes).
   * 
   * @param scopeToCheck The scope to check.
   * @return Whether the given scope is the same as or contained in this scope.
   */
  public boolean containsScope(SecurityScope scopeToCheck) {
    if (this == scopeToCheck)
      return true;

    for (SecurityScope containedScope : containedScopes)
      if (containedScope.containsScope(scopeToCheck))
        return true;

    return false;
  }

  /**
   * Returns the name of the scope.
   * 
   * @return The name of the scope - never {@code null}
   */
  @Override
  public String toString() {
    return getName();
  }

  /**
   * Returns the matching scope for the given scope name.
   * 
   * @param scopeName The scope name.
   * @return The matching scope for the given scope name or {@code null} if no
   *         scope with the given name was found.
   */
  public static SecurityScope fromName(String scopeName) {
    if (scopeName != null)
      for (SecurityScope scope : values())
        if (scope.getName().equals(scopeName))
          return scope;

    return null;
  }
}
