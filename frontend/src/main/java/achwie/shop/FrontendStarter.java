package achwie.shop;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

import achwie.shop.auth.SecurityFilterProxy;
import achwie.shop.auth.SessionService;
import achwie.shop.util.security.SecurityScope;
import achwie.shop.util.security.http.SecurityScopeDeserializer;

/**
 * 
 * @author 02.01.2016, Achim Wiedemann
 */
@SpringBootApplication
@Configuration
public class FrontendStarter {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(FrontendStarter.class, args);
  }

  @Bean
  public Filter createHystrixContextInitializationFilter() {
    return new HystrixContextInitializerFilter();
  }

  @Bean
  @Autowired
  public Filter createSecurityFilterProxy(SessionService sessionService) {
    return new SecurityFilterProxy(sessionService);
  }

  @Bean
  public ServletRegistrationBean createHystrixMetricsStreamServlet() {
    return new ServletRegistrationBean(new HystrixMetricsStreamServlet(), "/hystrix.stream");
  }

  @Bean
  public Module createJacksonModule() {
    final SimpleModule jacksonModule = new SimpleModule();

    jacksonModule.addDeserializer(SecurityScope.class, new SecurityScopeDeserializer());

    return jacksonModule;
  }
}
