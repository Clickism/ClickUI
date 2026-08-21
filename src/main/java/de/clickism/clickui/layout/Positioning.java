package de.clickism.clickui.layout;

/**
 * Represents the positioning of an element within its parent container.
 *
 * @param type The type of positioning (LAYOUT, ABSOLUTE, RELATIVE)
 * @param x    The x position of the element
 * @param y    The y position of the element
 */
public record Positioning(
    Type type,
    int x,
    int y
) {
    /**
     * The type of positioning.
     */
    public enum Type {
        /**
         * The element is positioned according to the layout engine.
         */
        LAYOUT,
        /**
         * The element is positioned absolutely within the screen.
         */
        ABSOLUTE,
        /**
         * The element is positioned relative to its parent container.
         */
        RELATIVE
    }

    /**
     * Creates a new Positioning instance with type LAYOUT.
     *
     * @return A new Positioning instance with type LAYOUT.
     */
    public static Positioning layout() {
        return new Positioning(Type.LAYOUT, 0, 0);
    }

    /**
     * Creates a new Positioning instance with type ABSOLUTE and the specified x and y positions.
     *
     * @param x The x position
     * @param y The y position
     * @return A new Positioning instance with type ABSOLUTE and the specified x and y positions.
     */
    public static Positioning absolute(int x, int y) {
        return new Positioning(Type.ABSOLUTE, x, y);
    }

    /**
     * Creates a new Positioning instance with type RELATIVE and the specified x and y positions.
     *
     * @param x The x position
     * @param y The y position
     * @return A new Positioning instance with type RELATIVE and the specified x and y positions.
     */
    public static Positioning relative(int x, int y) {
        return new Positioning(Type.RELATIVE, x, y);
    }
}
