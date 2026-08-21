package de.clickism.clickui.layout;

/**
 * Represents the axis of a layout, either horizontal or vertical.
 */
public enum Axis {
    /**
     * Represents the horizontal axis.
     */
    HORIZONTAL,
    /**
     * Represents the vertical axis.
     */
    VERTICAL;

    /**
     * Returns the opposite axis of the current axis.
     *
     * @return the opposite axis
     */
    public Axis opposite() {
        return isHorizontal()
            ? VERTICAL
            : HORIZONTAL;
    }

    /**
     * Checks if the current axis is horizontal.
     *
     * @return true if the current axis is horizontal, false otherwise
     */
    public boolean isHorizontal() {
        return this == HORIZONTAL;
    }
}
