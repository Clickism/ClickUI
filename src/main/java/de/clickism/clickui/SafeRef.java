package de.clickism.clickui;

import org.jetbrains.annotations.NotNull;

/**
 * Not-null ref implementation.
 * A value must be set before calling {@link #get()} or it will throw an exception.
 *
 * @param <T> the type of the value being referenced
 */
public class SafeRef<T> implements Ref<T> {
    private T value;

    /**
     * Creates a new SafeRef instance that must be set before it can be used.
     */
    public SafeRef() {
        this.value = null;
    }

    @Override
    public @NotNull T get() {
        if (value == null) {
            throw new IllegalStateException("Ref value is not set");
        }
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }
}
