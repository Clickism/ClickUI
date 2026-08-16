package de.clickism.clickui.event;

/**
 * Generic interface for event listeners.
 *
 * @param <E> The type of event this listener handles.
 */
public interface EventListener<E extends Event> {
    /**
     * Handles the given event.
     *
     * @param event The event to handle.
     */
    void handle(E event);
}
