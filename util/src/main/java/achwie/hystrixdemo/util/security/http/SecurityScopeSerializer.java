package achwie.hystrixdemo.util.security.http;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import achwie.hystrixdemo.util.security.SecurityScope;

/**
 * Serializes enum items of {@link SecurityScope} using
 * {@link SecurityScope#getName()}.
 * 
 * @author 23.06.2017, Achim Wiedemann
 * @see SecurityScopeDeserializer
 */
public class SecurityScopeSerializer extends JsonSerializer<SecurityScope> {
  @Override
  public void serialize(SecurityScope value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.writeString(value.getName());
  }

  @Override
  public Class<SecurityScope> handledType() {
    return SecurityScope.class;
  }
}
