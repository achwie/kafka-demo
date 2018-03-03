package achwie.shop.util.security.http;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Test;

import achwie.shop.util.security.SecurityScope;
import achwie.shop.util.security.http.SecurityScopeUtils;

/**
 * 
 * @author 23.06.2017, Achim Wiedemann
 *
 */
public class SecurityScopeUtilsTest {
  @Test
  public void parseScopes_shouldReturnScopes_whenGivenScopeListAsStringWithMultipleScopes() {
    final Set<SecurityScope> scopes = SecurityScopeUtils.parseScopes("visitor:shopper");

    assertEquals(EnumSet.of(SecurityScope.VISITOR, SecurityScope.SHOPPER), scopes);
  }

  @Test
  public void parseScopes_shouldReturnScopes_whenGivenScopeListAsStringWithSingleScope() {
    final Set<SecurityScope> scopes = SecurityScopeUtils.parseScopes("visitor");

    assertEquals(EnumSet.of(SecurityScope.VISITOR), scopes);
  }

  @Test
  public void parseScopes_shouldReturnEmptySet_whenGivenEmptyString() {
    final Set<SecurityScope> scopes = SecurityScopeUtils.parseScopes("");

    assertEquals(EnumSet.noneOf(SecurityScope.class), scopes);
  }

  @Test
  public void scopesToString_shouldReturnScopesString_whenGivenScopeListAsStringWithMultipleScopes() {
    final String scopesStr = SecurityScopeUtils.scopesToString(EnumSet.of(SecurityScope.VISITOR, SecurityScope.SHOPPER));

    assertEquals("visitor:shopper", scopesStr);
  }

  @Test
  public void scopesToString_shouldReturnScopesString_whenGivenScopeListAsStringWithSingleScope() {
    final String scopesStr = SecurityScopeUtils.scopesToString(EnumSet.of(SecurityScope.VISITOR));

    assertEquals("visitor", scopesStr);
  }

  @Test
  public void scopesToString_shouldReturnEmptyString_whenGivenEmptySet() {
    final String scopesStr = SecurityScopeUtils.scopesToString(EnumSet.noneOf(SecurityScope.class));

    assertEquals("", scopesStr);
  }
}
