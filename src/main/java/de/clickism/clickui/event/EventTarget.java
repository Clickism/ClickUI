package de.clickism.clickui.event;

import de.clickism.clickui.event.events.*;
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

    /**
     * Registers a listener for mouse release events.
     *
     * @param listener The listener to handle mouse release events.
     * @return The current instance of the EventTarget for method chaining.
     */
    default S onRelease(EventListener<MouseReleaseEvent> listener) {
        return on(MouseReleaseEvent.class, listener);
    }

    /**
     * Registers a listener for key press events.
     *
     * @param listener The listener to handle key press events.
     * @return The current instance of the EventTarget for method chaining.
     */
    default S onKeyPress(EventListener<KeyPressEvent> listener) {
        return on(KeyPressEvent.class, listener);
    }

    /**
     * Registers a listener for mouse scroll events.
     *
     * @param listener The listener to handle mouse scroll events.
     * @return The current instance of the EventTarget for method chaining.
     */
    default S onScroll(EventListener<MouseScrollEvent> listener) {
        return on(MouseScrollEvent.class, listener);
    }

    /**
     * Registers a listener for mouse enter events.
     *
     * @param listener The listener to handle mouse enter events.
     * @return The current instance of the EventTarget for method chaining.
     */
    default S onMouseEnter(EventListener<MouseEnterEvent> listener) {
        return on(MouseEnterEvent.class, listener);
    }

    /**
     * Registers a listener for mouse exit events.
     *
     * @param listener The listener to handle mouse exit events.
     * @return The current instance of the EventTarget for method chaining.
     */
    default S onMouseExit(EventListener<MouseExitEvent> listener) {
        return on(MouseExitEvent.class, listener);
    }
}
