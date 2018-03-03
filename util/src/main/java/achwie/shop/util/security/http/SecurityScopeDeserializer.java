package achwie.shop.util.security.http;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import achwie.shop.util.security.SecurityScope;

/**
 * Uses {@link SecurityScope#fromName(String)} method to parse string values
 * into enum items.
 * 
 * @author 23.06.2017, Achim Wiedemann
 * @see SecurityScopeSerializer
 */
public class SecurityScopeDeserializer extends JsonDeserializer<SecurityScope> {
  @Override
  public SecurityScope deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
    final String scopeName = parser.getValueAsString();

    return SecurityScope.fromName(scopeName);
  }

  @Override
  public Class<?> handledType() {
    return SecurityScope.class;
  }
}
