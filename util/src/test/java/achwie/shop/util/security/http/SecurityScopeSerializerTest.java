package achwie.shop.util.security.http;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import achwie.shop.util.security.SecurityScope;
import achwie.shop.util.security.http.SecurityScopeSerializer;

/**
 * 
 * @author 23.06.2017, Achim Wiedemann
 *
 */
public class SecurityScopeSerializerTest {
  private JsonFactory jsonFac;

  @Before
  public void setUp() {
    final SimpleModule testModule = new SimpleModule();
    testModule.addSerializer(new SecurityScopeSerializer());

    final ObjectMapper mapper = new ObjectMapper().registerModule(testModule);

    jsonFac = new JsonFactory(mapper);
  }

  @Test
  public void serialize_shouldSerializeSecurityScopeUsingFieldName() throws IOException {
    final Set<SecurityScope> scopes = EnumSet.of(SecurityScope.VISITOR, SecurityScope.SHOPPER);
    final StringWriter out = new StringWriter();

    jsonFac.createGenerator(out).writeObject(scopes);

    assertEquals("[\"visitor\",\"shopper\"]", out.toString());
  }
}
