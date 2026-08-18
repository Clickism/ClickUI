package de.clickism.clickui.layout;

/**
 * Represents the size of a UI element.
 *
 * @param type  The type of size (fixed, fit, or grow)
 * @param value The value of the size (only used for fixed size)
 */
// TODO: Configurable min and max in sizing
public record Sizing(Type type, int value, Integer min, Integer max) {
    /**
     * Creates a new Sizing instance with type FIXED and the specified value.
     *
     * @param value The fixed size value.
     * @return A new Sizing instance with type FIXED and the specified value.
     */
    public static Sizing fixed(int value) {
        return new Sizing(Type.FIXED, value, value, value);
    }

    /*+
     * Creates a new Sizing instance with type FIT.
     *
     * @return A new Sizing instance with type FIT.
     */
    public static Sizing fit() {
        return new Sizing(Type.FIT, 0, null, null);
    }

    /**
     * Creates a new Sizing instance with type GROW.
     *
     * @return A new Sizing instance with type GROW.
     */
    public static Sizing grow() {
        return new Sizing(Type.GROW, 0, null, null);
    }

    /**
     * Sets the minimum size for this Sizing instance.
     *
     * @param min The minimum size to set.
     * @return A new Sizing instance with the specified minimum size.
     */
    public Sizing min(int min) {
        if (type == Type.FIXED) {
            throw new IllegalStateException("Cannot set min size for FIXED sizing");
        }
        return new Sizing(type, value, min, max);
    }

    /**
     * Sets the maximum size for this Sizing instance.
     *
     * @param max The maximum size to set.
     * @return A new Sizing instance with the specified maximum size.
     */
    public Sizing max(int max) {
        if (type == Type.FIXED) {
            throw new IllegalStateException("Cannot set max size for FIXED sizing");
        }
        return new Sizing(type, value, min, max);
    }

    /**
     * Sets the minimum size for this Sizing instance if it is not already set.
     *
     * @param defaultMin The default minimum size to set if min is not already set.
     * @return A new Sizing instance with the specified minimum size if min was not already set, otherwise returns the current instance.
     */
    public Sizing orDefaultMin(int defaultMin) {
        if (min == null) {
            return min(defaultMin);
        }
        return this;
    }

    /**
     * Returns the value of the size if it is fixed, otherwise returns 0.
     *
     * @return The value of the size if it is fixed, otherwise 0.
     */
    @Override
    public int value() {
        return type == Type.FIXED
               ? value
               : 0;
    }

    /**
     * The type of size.
     */
    public enum Type {
        FIXED,
        FIT,
        GROW
    }
}
