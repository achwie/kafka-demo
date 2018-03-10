package achwie.shop.event.impl;

/**
 * Indicates a problem with (de-) serialization.
 * 
 * @author 10.03.2018, Achim Wiedemann
 *
 */
public class SerializationException extends Exception {
  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SerializationException(String message) {
    super(message);
  }
}
