package de.clickism.clickui.layout;

/**
 * Represents a size with width and height.
 *
 * @param width  width
 * @param height height
 */
public record Size(int width, int height) {
    /**
     * A sizing with zero width and height.
     */
    public static Size ZERO = new Size(0, 0);

    /**
     * Returns the main size based on the given axis.
     *
     * @param axis the layout axis
     * @return the main size (width for horizontal, height for vertical)
     */
    public int mainSize(LayoutAxis axis) {
        return axis.isHorizontal()
            ? width
            : height;
    }

    /**
     * Returns the cross size based on the given axis.
     *
     * @param axis the layout axis
     * @return the cross size (height for horizontal, width for vertical)
     */
    public int crossSize(LayoutAxis axis) {
        return axis.isHorizontal()
            ? height
            : width;
    }
}
