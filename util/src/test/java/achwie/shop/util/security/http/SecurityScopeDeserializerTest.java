package achwie.shop.util.security.http;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import achwie.shop.util.security.SecurityScope;
import achwie.shop.util.security.http.SecurityScopeDeserializer;

/**
 * 
 * @author 23.06.2017, Achim Wiedemann
 *
 */
public class SecurityScopeDeserializerTest {
  private JsonFactory jsonFac;

  @Before
  public void setUp() {
    final SimpleModule testModule = new SimpleModule();
    testModule.addDeserializer(SecurityScope.class, new SecurityScopeDeserializer());

    final ObjectMapper mapper = new ObjectMapper().registerModule(testModule);

    jsonFac = new JsonFactory(mapper);
  }

  @Test
  public void deserialize_shouldDeserializeJsonUsingFieldName() throws IOException {
    final StringReader in = new StringReader("[\"visitor\",\"shopper\"]");

    final SecurityScope[] scopes = jsonFac.createParser(in).readValueAs(SecurityScope[].class);

    assertArrayEquals(new SecurityScope[] { SecurityScope.VISITOR, SecurityScope.SHOPPER }, scopes);
  }
}
