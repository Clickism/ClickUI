package de.clickism.clickui.layout;

/**
 * Represents padding with top, right, bottom, and left values.
 *
 * @param top    the top padding
 * @param right  the right padding
 * @param bottom the bottom padding
 * @param left   the left padding
 */
public record Padding(int top, int right, int bottom, int left) {
    /**
     * A padding with zero values for all sides.
     */
    public static Padding ZERO = new Padding(0, 0, 0, 0);

    /**
     * Creates a uniform padding with the same value for all sides.
     *
     * @param padding the padding value for all sides
     * @return a new Padding instance with uniform values
     */
    public static Padding create(int padding) {
        return new Padding(padding, padding, padding, padding);
    }

    /**
     * Creates a new Padding instance with the specified values for each side.
     *
     * @param top    the top padding
     * @param right  the right padding
     * @param bottom the bottom padding
     * @param left   the left padding
     * @return a new Padding instance with the specified values
     */
    public static Padding create(int top, int right, int bottom, int left) {
        return new Padding(top, right, bottom, left);
    }

    /**
     * Creates a new Padding instance with the specified vertical and horizontal values.
     *
     * @param vertical   the vertical padding (top and bottom)
     * @param horizontal the horizontal padding (left and right)
     * @return a new Padding instance with the specified vertical and horizontal values
     */
    public static Padding create(int vertical, int horizontal) {
        return new Padding(vertical, horizontal, vertical, horizontal);
    }

    /**
     * Calculates the total horizontal padding (left + right).
     *
     * @return the total horizontal padding
     */
    public int horizontal() {
        return left + right;
    }

    /**
     * Calculates the total vertical padding (top + bottom).
     *
     * @return the total vertical padding
     */
    public int vertical() {
        return top + bottom;
    }

    /**
     * Calculates the main padding based on the given layout axis.
     *
     * @param axis the layout axis
     * @return the main padding value (horizontal or vertical)
     */
    public int mainPadding(Axis axis) {
        return axis.isHorizontal()
            ? horizontal()
            : vertical();
    }

    /**
     * Calculates the cross padding based on the given layout axis.
     *
     * @param axis the layout axis
     * @return the cross padding value (vertical or horizontal)
     */
    public int crossPadding(Axis axis) {
        return axis.isHorizontal()
            ? vertical()
            : horizontal();
    }
}
