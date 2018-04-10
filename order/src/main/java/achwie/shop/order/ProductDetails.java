package achwie.shop.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author 09.04.2018, Achim Wiedemann
 *
 */

public class ProductDetails {
  private final String id;
  private final String name;

  @JsonCreator
  public ProductDetails(@JsonProperty("id") String id, @JsonProperty("name") String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
