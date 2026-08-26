package de.clickism.clickui.state;

import de.clickism.clickui.util.Self;

/**
 * Represents an object that holds the state of a UI element.
 *
 * @param <S> the type of the state holder, which extends ElementStateHolder
 */
public interface ElementStateHolder<S extends ElementStateHolder<S>>
    extends Self<S> {

    /**
     * Returns the state of the UI element.
     *
     * @return the state of the UI element
     */
    ElementState state();

    /**
     * Checks if <strong>this element itself</strong> is hovered.
     *
     * @return true if the element is hovered, false otherwise
     */
    default boolean hoveredSelf() {
        return state().hovered;
    }

    /**
     * Checks if the element <strong>or any of its children</strong> are hovered.
     *
     * @return true if the element or any of its children are hovered, false otherwise
     */
    default boolean hovered() {
        if (hoveredSelf()) {
            return true;
        }
        var element = state().element;
        for (var child : element.children()) {
            if (child.hovered()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the hovered state of the element.
     *
     * @param hovered the new hovered state
     * @return this instance for method chaining
     */
    default S hovered(boolean hovered) {
        state().hovered = hovered;
        return self();
    }

    /**
     * Checks if the element is disabled.
     *
     * @return true if the element is disabled, false otherwise
     */
    default boolean disabled() {
        return state().disabled;
    }

    /**
     * Sets the disabled state of the element.
     *
     * @param disabled the new disabled state
     * @return this instance for method chaining
     */
    default S disabled(boolean disabled) {
        state().disabled = disabled;
        return self();
    }

    /**
     * Checks if the element is focused.
     *
     * @return true if the element is focused, false otherwise
     */
    default boolean focused() {
        return state().focused;
    }

    /**
     * Sets the focused state of the element.
     *
     * @param focused the new focused state
     * @return this instance for method chaining
     */
    default S focused(boolean focused) {
        state().focused = focused;
        return self();
    }
}
