package achwie.shop.event.impl.kafka;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
public interface PayloadTypeMapper {
  public Class<?> getPayloadTypeFor(int eventType, int eventVersion);
}
