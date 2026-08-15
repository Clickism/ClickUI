package de.clickism.clickui;

/**
 * A generic interface that provides a method to return the current instance as a specific type.
 *
 * @param <S> the type of the current instance
 */
public interface Self<S> {
    /**
     * Returns the current instance as the specified type.
     *
     * @return the current instance cast to the specified type
     */
    @SuppressWarnings("unchecked")
    default S self() {
        return (S) this;
    }
}
