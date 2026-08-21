package de.clickism.clickui.layout;

import java.util.Objects;

/**
 * Represents the size of a UI element.
 *
 * @param type  The type of size (fixed, fit, or grow)
 * @param fixed The fixed size value, or null if not fixed
 * @param min   The minimum size value, or null to let engine decide (same as fixed for fixed type)
 * @param max   The maximum size value, or null to let engine decide (same as fixed for fixed type)
 */
// TODO: Configurable min and max in sizing
public record Sizing(Type type, Integer fixed, Integer min, Integer max) {
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
        return new Sizing(Type.FIT, null, null, null);
    }

    /**
     * Creates a new Sizing instance with type GROW.
     *
     * @return A new Sizing instance with type GROW.
     */
    public static Sizing grow() {
        return new Sizing(Type.GROW, null, null, null);
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
        return new Sizing(type, fixed, min, max);
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
        return new Sizing(type, fixed, min, max);
    }

    @Override
    public Integer fixed() {
        return type == Type.FIXED
            ? Objects.requireNonNull(fixed)
            : null;
    }

    /**
     * Checks if the sizing type is GROW.
     *
     * @return true if the sizing type is GROW, false otherwise.
     */
    public boolean isGrow() {
        return type == Type.GROW;
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
