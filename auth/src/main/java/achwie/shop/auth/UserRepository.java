package achwie.shop.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import achwie.shop.util.SimpleCsvReader;
import achwie.shop.util.security.SecurityScope;
import achwie.shop.util.security.http.SecurityScopeUtils;

/**
 * 
 * @author 21.11.2015, Achim Wiedemann
 */
// DON'T DO THIS AT HOME!
@Component
public class UserRepository {
  private final Map<String, User> users = new HashMap<>();

  {
    try (final InputStream is = UserRepository.class.getResourceAsStream("/test-data-users.csv")) {
      SimpleCsvReader.readLines(is, values -> {
        if (values.length != 4)
          return;

        String id = values[0];
        String name = values[1];
        String pass = values[2];
        Set<SecurityScope> scopes = SecurityScopeUtils.parseScopes(values[3]);

        users.put(key(name, pass), new User(id, name, scopes));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public User findUserByCredentials(String username, String password) {
    final String key = key(username, password);

    return users.get(key);
  }

  private String key(String username, String password) {
    return username + ":" + password;
  }
}
