package de.clickism.clickui.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages event listeners and dispatches events to them.
 */
public class EventManager {
    private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

    /**
     * Registers an event listener for a specific event type.
     *
     * @param eventType The class of the event type to listen for.
     * @param listener  The listener to register.
     * @param <E>       The type of the event.
     */
    public <E extends Event> void registerListener(
        Class<E> eventType,
        EventListener<E> listener
    ) {
        listeners
            .computeIfAbsent(eventType, k -> new ArrayList<>())
            .add(listener);
    }

    /**
     * Removes a specific listener for a given event type.
     *
     * @param eventType The class of the event type to remove the listener from.
     * @param listener  The listener to remove.
     */
    public void removeListener(Class<?> eventType, EventListener<?> listener) {
        var eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    /**
     * Clears all registered listeners from the event manager.
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * Fires an event to all registered listeners for the event's type.
     *
     * @param event The event to fire.
     * @param <E>   The type of the event.
     */
    public <E extends Event> void fireEvent(E event) {
        var eventListeners = listeners.get(event.getClass());

        if (eventListeners == null) {
            return;
        }

        for (var listener : eventListeners) {
            @SuppressWarnings("unchecked")
            EventListener<E> typedListener = (EventListener<E>) listener;
            typedListener.handle(event);
        }
    }
}
