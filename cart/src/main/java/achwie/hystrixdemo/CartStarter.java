package achwie.hystrixdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import achwie.hystrixdemo.util.security.SecurityContextProvider;
import achwie.hystrixdemo.util.security.UserFilter;

/**
 * 
 * @author 02.01.2016, Achim Wiedemann
 */
@SpringBootApplication
public class CartStarter {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(CartStarter.class, args);
  }

  @Bean
  public SecurityContextProvider createSecurityContextProvider() {
    return new SecurityContextProvider();
  }

  @Autowired
  @Bean
  public FilterRegistrationBean createUserFilter(SecurityContextProvider securityContextProvider) {
    return new FilterRegistrationBean(new UserFilter(securityContextProvider));
  }
}
