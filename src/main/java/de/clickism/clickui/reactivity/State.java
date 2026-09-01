package de.clickism.clickui.reactivity;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a reactive state that holds a value and notifies its parent component when the value changes.
 *
 * @param <T> The type of the value held by this state.
 */
public class State<T> {
    private T value;
    private final Consumer<T> callback;

    /**
     * Creates a new State instance with the specified initial value and parent component.
     *
     * @param value    The initial value of the state.
     * @param callback A callback function that is called whenever the state value changes.
     */
    public State(T value, Consumer<T> callback) {
        this.value = value;
        this.callback = callback;
    }

    /**
     * Returns the current value of the state.
     *
     * @return The current value of the state.
     */
    public T get() {
        return value;
    }

    /**
     * Updates the value of the state and notifies the parent component of the change.
     *
     * @param value The new value to set for the state.
     */
    public void update(T value) {
        this.value = value;
        this.callback.accept(value);
    }

    /**
     * Updates the value of the state using the provided updater function and notifies the parent component of the change.
     *
     * @param updater A function that takes the current value and returns the new value.
     */
    public void update(Function<T, T> updater) {
        this.update(updater.apply(this.value));
    }
}
