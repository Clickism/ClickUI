package de.clickism.clickui.layout;

/**
 * Represents a rectangle with position and size.
 *
 * @param x      the x position
 * @param y      the y position
 * @param width  the width
 * @param height the height
 */
public record Rect(int x, int y, int width, int height) {
    /**
     * A rectangle with zero position and size.
     */
    public static Rect ZERO = new Rect(0, 0, 0, 0);

    /**
     * Returns the main size of the rectangle based on the given layout axis.
     *
     * @param axis the layout axis
     * @return the main size
     */
    public int mainSize(LayoutAxis axis) {
        return axis.isHorizontal()
            ? width
            : height;
    }

    /**
     * Returns the main size of the rectangle based on the layout axis of the given layoutable.
     *
     * @param layoutable the layoutable object
     * @return the main size
     */
    public int mainSize(Layoutable<?> layoutable) {
        return mainSize(layoutable.axis());
    }

    /**
     * Returns the cross size of the rectangle based on the given layout axis.
     *
     * @param axis the layout axis
     * @return the cross size
     */
    public int crossSize(LayoutAxis axis) {
        return axis.isHorizontal()
            ? height
            : width;
    }

    /**
     * Returns the cross size of the rectangle based on the layout axis of the given layoutable.
     *
     * @param layoutable the layoutable object
     * @return the cross size
     */
    public int crossSize(Layoutable<?> layoutable) {
        return crossSize(layoutable.axis());
    }

    /**
     * Checks if the rectangle contains the given point.
     *
     * @param point the point to check
     * @return true if the rectangle contains the point, false otherwise
     */
    public boolean contains(Point point) {
        return contains(point.x(), point.y());
    }

    /**
     * Checks if the rectangle contains the given point (px, py).
     *
     * @param px the x coordinate of the point
     * @param py the y coordinate of the point
     * @return true if the rectangle contains the point, false otherwise
     */
    public boolean contains(double px, double py) {
        return px >= x && px < x + width && py >= y && py < y + height;
    }

    /**
     * Calculates the area of the rectangle.
     *
     * @return the area of the rectangle
     */
    public int area() {
        return width * height;
    }

    /**
     * Checks if the rectangle is empty (i.e., has an area of zero).
     *
     * @return true if the rectangle is empty, false otherwise
     */
    public boolean isEmpty() {
        return area() == 0;
    }

    /**
     * Returns a new rectangle with the specified width, keeping the same position and height.
     *
     * @param width the new width
     * @return a new rectangle with the specified width
     */
    public Rect withWidth(int width) {
        return new Rect(x, y, width, height);
    }

    /**
     * Returns a new rectangle with the specified height, keeping the same position and width.
     *
     * @param height the new height
     * @return a new rectangle with the specified height
     */
    public Rect withHeight(int height) {
        return new Rect(x, y, width, height);
    }

    /**
     * Returns a new rectangle with the specified position (x, y), keeping the same width and height.
     *
     * @param x the new x position
     * @param y the new y position
     * @return a new rectangle with the specified position
     */
    public Rect withPosition(int x, int y) {
        return new Rect(x, y, width, height);
    }
}
