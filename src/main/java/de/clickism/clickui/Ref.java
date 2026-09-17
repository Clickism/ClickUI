package de.clickism.clickui;

/**
 * A wrapper interface that holds a reference to a value of type T.
 * <p>
 * Refs are especially useful when building UI trees as you can use the {@link UiElement#ref(SafeRef)}
 * method to set a ref to a UI element. Allowing you to access the element later without needing to traverse the tree,
 * keeping the declarative style of building the UI.
 *
 * @param <T> the type of the value being referenced
 */
public interface Ref<T> {
    /**
     * Returns the value of this reference.
     *
     * @return the value of this reference
     */
    T get();

    /**
     * Sets the value of this reference.
     *
     * @param value the value to set
     */
    void set(T value);

    /**
     * Creates a new SafeRef instance that must be set before it can be used.
     *
     * @param <T> the type of the value being referenced
     * @return a new SafeRef instance
     */
    static <T> SafeRef<T> notNull() {
        return new SafeRef<>();
    }

    /**
     * Creates a new NullableRef instance that can hold a null value.
     *
     * @param <T> the type of the value being referenced
     * @return a new NullableRef instance
     */
    static <T> NullableRef<T> nullable() {
        return new NullableRef<>();
    }
}
