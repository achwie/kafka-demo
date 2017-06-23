package achwie.hystrixdemo.util.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author 23.06.2017, Achim Wiedemann
 *
 */
public class SecurityScopeTest {
  @Test
  public void fromName_shouldReturnShopperScope_whenGivenShopperName() {
    assertSame(SecurityScope.SHOPPER, SecurityScope.fromName("shopper"));
  }

  @Test
  public void containsScope_shouldReturnTrue_whenGivenScopeMatchesNestedScope() {
    assertTrue(SecurityScope.ADMIN.containsScope(SecurityScope.VISITOR));
  }

  @Test
  public void containsScope_shouldReturnFalse_whenGivenScopeNotMatchesNestedScope() {
    assertFalse(SecurityScope.VISITOR.containsScope(SecurityScope.ADMIN));
  }

  @Test
  public void isScope_shouldReturnNull_whenGivenInvalidScopeName() {
    assertNull(SecurityScope.fromName("sHopper"));
  }
}
