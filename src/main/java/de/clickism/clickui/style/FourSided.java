package de.clickism.clickui.style;

import java.util.function.Function;

/**
 * A generic record representing values for four sides: top, right, bottom, and left.
 *
 * @param <T>    the type of the values for each side
 * @param top    the value for the top side
 * @param right  the value for the right side
 * @param bottom the value for the bottom side
 * @param left   the value for the left side
 */
public record FourSided<T>(
    T top,
    T right,
    T bottom,
    T left
) {
    /**
     * An enumeration representing the four sides: TOP, RIGHT, BOTTOM, and LEFT.
     */
    public enum Side {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    /**
     * Creates a new FourSided instance with the specified values for each side.
     *
     * @param top    the value for the top side
     * @param right  the value for the right side
     * @param bottom the value for the bottom side
     * @param left   the value for the left side
     * @param <T>    the type of the values for each side
     * @return a new FourSided instance
     */
    public static <T> FourSided<T> of(T top, T right, T bottom, T left) {
        return new FourSided<>(top, right, bottom, left);
    }

    /**
     * Creates a new FourSided instance with the same value for all sides.
     *
     * @param value the value to be used for all sides
     * @param <T>   the type of the value
     * @return a new FourSided instance with the same value for all sides
     */
    public static <T> FourSided<T> of(T value) {
        return new FourSided<>(value, value, value, value);
    }

    public FourSided<T> withTop(T newTop) {
        return new FourSided<>(newTop, this.right, this.bottom, this.left);
    }

    public FourSided<T> withRight(T newRight) {
        return new FourSided<>(this.top, newRight, this.bottom, this.left);
    }

    public FourSided<T> withBottom(T newBottom) {
        return new FourSided<>(this.top, this.right, newBottom, this.left);
    }

    public FourSided<T> withLeft(T newLeft) {
        return new FourSided<>(this.top, this.right, this.bottom, newLeft);
    }

    public FourSided<T> withAllSides(Function<T, T> updater) {
        return new FourSided<>(
            updater.apply(this.top),
            updater.apply(this.right),
            updater.apply(this.bottom),
            updater.apply(this.left)
        );
    }

    /**
     * Creates a new FourSided instance with the specified side updated to the new value.
     *
     * @param side     the side to update
     * @param newValue the new value for the specified side
     * @return a new FourSided instance with the updated value for the specified side
     */
    public FourSided<T> with(Side side, T newValue) {
        return switch (side) {
            case TOP -> withTop(newValue);
            case RIGHT -> withRight(newValue);
            case BOTTOM -> withBottom(newValue);
            case LEFT -> withLeft(newValue);
        };
    }

    /**
     * Retrieves the value for the specified side.
     *
     * @param side the side for which to retrieve the value
     * @return the value corresponding to the specified side
     */
    public T get(Side side) {
        return switch (side) {
            case TOP -> top;
            case RIGHT -> right;
            case BOTTOM -> bottom;
            case LEFT -> left;
        };
    }
}
