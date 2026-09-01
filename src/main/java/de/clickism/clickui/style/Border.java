package de.clickism.clickui.style;

import de.clickism.clickui.UiColor;

/**
 * Represents a border style for UI elements, including its width, position, and color.
 *
 * @param width    the width of the border in pixels
 * @param position the position of the border relative to the element (inside, center, or outside)
 * @param color    the color of the border
 */
public record Border(
    int width,
    Position position,
    UiColor color
) {
    /**
     * Checks if the border is visible based on its width and color alpha value.
     *
     * @return true if the border is visible, false otherwise
     */
    public boolean isVisible() {
        return width > 0 && color.alpha() > 0;
    }

    /**
     * Represents the position of a border relative to the element it surrounds.
     */
    public enum Position {
        OUTSIDE,
        CENTER,
        INSIDE
    }
}
