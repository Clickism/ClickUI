package de.clickism.clickui;

/**
 * Represents the size of a UI element.
 *
 * @param type  The type of size (fixed, fit, or grow)
 * @param value The value of the size (only used for fixed size)
 */
public record Sizing(Type type, int value) {
    /**
     * The type of size.
     */
    public enum Type {
        FIXED,
        FIT,
        GROW
    }

    public static Sizing fixed(int value) {
        return new Sizing(Type.FIXED, value);
    }

    public static Sizing fit() {
        return new Sizing(Type.FIT, 0);
    }

    public static Sizing grow() {
        return new Sizing(Type.GROW, 0);
    }

    /**
     * Returns the value of the size if it is fixed, otherwise returns 0.
     *
     * @return The value of the size if it is fixed, otherwise 0.
     */
    @Override
    public int value() {
        return type == Type.FIXED ? value : 0;
    }
}
