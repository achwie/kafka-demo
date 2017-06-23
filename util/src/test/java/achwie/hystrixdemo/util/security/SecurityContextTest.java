package achwie.hystrixdemo.util.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

import org.junit.Test;

/**
 * 
 * @author 23.06.2017, Achim Wiedemann
 *
 */
public class SecurityContextTest {
  private final SecurityContext nonAdminSecurityContext = new SecurityContext("test", EnumSet.of(SecurityScope.SHOPPER, SecurityScope.VISITOR));

  @Test
  public void containsScope_shouldReturnTrue_whenContextContainsScopeWithGivenName() {
    assertTrue(nonAdminSecurityContext.containsScope(SecurityScope.VISITOR));
  }

  @Test
  public void containsScope_shouldReturnFalse_whenContextNotContainsScopeWithGivenName() {
    assertFalse(nonAdminSecurityContext.containsScope(SecurityScope.ADMIN));
  }
}
