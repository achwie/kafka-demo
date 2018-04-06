package achwie.shop.event.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Holds a list of {@link EventHandler event handlers} for processing events and
 * provides a lookup methods for the handlers.
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
// TODO: The EventTypeMapper part of this class is total crap
public class EventHandlerChain implements EventTypeMapper {
  private final List<EventHandler<?>> handlers = new ArrayList<>();

  /**
   * Registers a new event handler.
   * 
   * @param eventHandler The handler to register
   */
  public void addEventHandler(EventHandler<?> eventHandler) {
    Objects.requireNonNull(eventHandler, "Given event handler must not be null!");

    handlers.add(eventHandler);
  }

  /**
   * Unregisters an event handler. If the handler was never registered nothing
   * happens.
   * 
   * @param eventHandler The handler to unregister
   */
  public void removeEventHandler(EventHandler<?> eventHandler) {
    Objects.requireNonNull(eventHandler, "Given event handler must not be null!");

    handlers.remove(eventHandler);
  }

  /**
   * Looks up the event handlers in the chain that can process the given event
   * type.
   * 
   * @param eventType The event type to look up the handlers for
   * @return The event handlers that can handle the given event type or an empty
   *         list if none was found
   */
  @SuppressWarnings("unchecked")
  public <T> List<EventHandler<T>> findEventHandlersFor(Class<T> eventType) {
    return handlers.stream()
        .filter(h -> h.getProcessableEventVersion().getEventType().equals(eventType))
        .map(h -> (EventHandler<T>) h)
        .collect(Collectors.toList());
  }

  /**
   * Looks up to which event type a certain type-code and version-code
   * combination is mapped to.
   * 
   * @param typeCode The type code
   * @param versionCode The version code
   * @return The first event type the given type- and version-code combination
   *         is matched to or {@code null} if not found.
   */
  @Override
  public Class<?> getEventTypeFor(int typeCode, int versionCode) {
    for (EventHandler<?> handler : handlers) {
      final EventVersion handlingEventVersion = handler.getProcessableEventVersion();

      if (handlingEventVersion.getTypeCode() == typeCode && handlingEventVersion.getVersionCode() == versionCode) {
        return ((EventHandler<?>) handler).getProcessableEventVersion().getEventType();
      }
    }

    return null;
  }

  /**
   * Looks up the {@link EventVersion event version} for a given event type.
   * 
   * @param eventType The event type to look up the event version for
   * @return The first matching event version or {@code null} if not found.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public EventVersion getEventVersionFor(Class eventType) {
    final List<EventHandler> handlersForType = findEventHandlersFor(eventType);

    return !handlersForType.isEmpty() ? handlersForType.get(0).getProcessableEventVersion() : null;
  }
}
