package de.clickism.clickui;

import org.jetbrains.annotations.ApiStatus;

/**
 * An interface for UI elements that can be wrapped to fit within a specified width.
 */
public interface Wrappable {
    /**
     * Wraps this UI element to fit within the specified maximum width.
     * <p>
     * This function should update the <strong>height</strong>
     * of the element's bounding box to reflect the new wrapped height.
     *
     * @param maxWidth The maximum width to wrap the element to.
     */
    @ApiStatus.Internal
    void wrap(int maxWidth);
}
