package achwie.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import achwie.shop.util.security.SecurityScope;
import achwie.shop.util.security.http.SecurityScopeSerializer;

/**
 * 
 * @author 02.01.2016, Achim Wiedemann
 */
@SpringBootApplication
public class AuthStarter {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(AuthStarter.class, args);
  }

  @Bean
  public Module createJacksonModule() {
    final SimpleModule jacksonModule = new SimpleModule();

    jacksonModule.addSerializer(SecurityScope.class, new SecurityScopeSerializer());

    return jacksonModule;
  }
}
