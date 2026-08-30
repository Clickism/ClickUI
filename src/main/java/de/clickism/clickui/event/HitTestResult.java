package de.clickism.clickui.event;

import de.clickism.clickui.UiElement;

/**
 * Represents the result of a hit test, containing the target element and the coordinates of the hit.
 *
 * @param target The element that was hit.
 * @param x      The x-coordinate of the hit.
 * @param y      The y-coordinate of the hit.
 */
public record HitTestResult(
    UiElement<?> target,
    int x,
    int y
) {
}
