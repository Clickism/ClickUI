package de.clickism.clickui.event;

import de.clickism.clickui.event.events.MouseClickEvent;
import de.clickism.clickui.util.Self;

/**
 * Represents an object that can have event listeners registered to it and can fire events.
 *
 * @param <S> The type of the EventTarget, used for method chaining.
 */
public interface EventTarget<S extends EventTarget<S>>
    extends Self<S> {

    /**
     * Returns the EventManager associated with this EventTarget.
     *
     * @return the EventManager instance
     */
    EventManager events();

    /**
     * Registers an event listener for a specific event type.
     *
     * @param eventType The class of the event type to listen for.
     * @param listener  The listener to register.
     * @param <E>       The type of the event.
     * @return The current instance of the EventTarget for method chaining.
     */
    default <E extends Event> S on(Class<E> eventType, EventListener<E> listener) {
        events().registerListener(eventType, listener);
        return self();
    }

    /**
     * Registers a listener for mouse click events.
     *
     * @param listener The listener to handle mouse click events.
     * @return The current instance of the EventTarget for method chaining.
     */
    default S onClick(EventListener<MouseClickEvent> listener) {
        return on(MouseClickEvent.class, listener);
    }
}
