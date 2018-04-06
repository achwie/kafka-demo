package achwie.shop.eventstore;

/**
 * Just in case we'd need some interface for all domain events
 * 
 * @author 17.03.2018, Achim Wiedemann
 *
 */
public interface DomainEvent {
  public Object getAggregateId();

}
