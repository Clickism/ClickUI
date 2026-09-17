package de.clickism.clickui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper class that holds a nullable reference to a value of type T.
 * This class provides methods to safely access and modify the value,
 * including options to get the value with default handling or throw an exception if not present.
 *
 * @param <T> the type of the value being referenced
 */
public class NullableRef<T> implements Ref<T> {
    private T value;

    /**
     * Creates a new NullableRef instance with no initial value.
     */
    public NullableRef() {
        this.value = null;
    }

    /**
     * Returns the value if present, otherwise returns null.
     *
     * @return the actual value if present, otherwise null
     */
    @Override
    public @Nullable T get() {
        return value;
    }

    /**
     * Sets the value of this NullableRef.
     *
     * @param value the value to set, can be null
     */
    @Override
    public void set(@Nullable T value) {
        this.value = value;
    }

    /**
     * Returns the value if present, otherwise throws an IllegalStateException.
     *
     * @return the actual value if present
     * @throws IllegalStateException if the value is not present
     */
    public @NotNull T getOrThrow() {
        if (value == null) {
            throw new IllegalStateException("NullableRef value is not set");
        }
        return value;
    }

    /**
     * Returns the value if present, otherwise returns the provided default value.
     *
     * @param defaultValue the value to return if the actual value is not present
     * @return the actual value if present, otherwise the default value
     */
    public T getOrDefault(T defaultValue) {
        return value != null
            ? value
            : defaultValue;
    }

    /**
     * Checks if the value is present (not null).
     *
     * @return true if the value is present, false otherwise
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Returns a SafeRef that mirrors this NullableRef.
     * The SafeRef will throw an exception if the value is not set when calling {@link SafeRef#get()}.
     * Calling {@link SafeRef#set(Object)} will set the value of this NullableRef.
     *
     * @return a Ref that wraps this NullableRef
     */
    public SafeRef<T> asSafeRef() {
        return new SafeRef<>() {
            @Override
            public @NotNull T get() {
                return NullableRef.this.getOrThrow();
            }

            @Override
            public void set(T value) {
                NullableRef.this.set(value);
            }
        };
    }
}
