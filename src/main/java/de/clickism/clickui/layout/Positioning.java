package de.clickism.clickui.layout;

public record Positioning(
    Type type,
    int x,
    int y
) {
    /**
     * The type of positioning.
     */
    public enum Type {
        LAYOUT,
        ABSOLUTE,
        RELATIVE
    }

    public static Positioning layout() {
        return new Positioning(Type.LAYOUT, 0, 0);
    }

    public static Positioning absolute(int x, int y) {
        return new Positioning(Type.ABSOLUTE, x, y);
    }

    public static Positioning relative(int x, int y) {
        return new Positioning(Type.RELATIVE, x, y);
    }
}
