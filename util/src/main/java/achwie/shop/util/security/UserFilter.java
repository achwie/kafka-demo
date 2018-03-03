package achwie.shop.util.security;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Extracts user and scope information from the request headers and sets it to
 * the given {@link SecurityContextProvider} in form of a
 * {@link SecurityContext}.
 * </p>
 * <p>
 * To register the filter:
 * </p>
 * 
 * <pre>
 * &#64;Bean
 * public SecurityContextProvider createSecurityContextProvider() {
 *   return new SecurityContextProvider();
 * }
 * 
 * &#64;Autowired
 * &#64;Bean
 * public FilterRegistrationBean createUserFilter(SecurityContextProvider securityContextProvider) {
 *   return new FilterRegistrationBean(new UserFilter(securityContextProvider));
 * }
 * </pre>
 * 
 * @author 16.06.2017, Achim Wiedemann
 *
 */
public class UserFilter implements Filter {
  private final SecurityContextProvider secContextProvider;

  public UserFilter(SecurityContextProvider secContextProvider) {
    this.secContextProvider = secContextProvider;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      final String user = httpRequest.getHeader(SecurityHeader.USER.getHeaderName());
      final String scopes = httpRequest.getHeader(SecurityHeader.SCOPES.getHeaderName());

      secContextProvider.setCurrentContext(createSecurityContext(user, scopes));
    }

    chain.doFilter(request, response);

    secContextProvider.setCurrentContext(null);
  }

  @Override
  public void destroy() {
  }

  private SecurityContext createSecurityContext(String user, String scopeNamesStr) {
    Set<SecurityScope> scopes = new HashSet<>();

    if (scopeNamesStr != null) {
      final String[] scopeNamesArr = scopeNamesStr.split(":");

      for (String scopeName : scopeNamesArr) {
        final SecurityScope scope = SecurityScope.fromName(scopeName);
        if (scope != null)
          scopes.add(scope);
      }
    }

    // Empty EnumSets need to be created differently
    final EnumSet<SecurityScope> scopeSet = !scopes.isEmpty() ? EnumSet.copyOf(scopes) : EnumSet.noneOf(SecurityScope.class);

    return new SecurityContext(user, scopeSet);
  }
}
