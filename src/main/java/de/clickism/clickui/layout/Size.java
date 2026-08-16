package de.clickism.clickui.layout;

/**
 * Represents a size with width and height.
 *
 * @param width  width
 * @param height height
 */
public record Size(int width, int height) {
    public static Size ZERO = new Size(0, 0);
}
