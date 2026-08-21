package de.clickism.clickui.state;

import de.clickism.clickui.util.Self;

public interface ElementStateHolder<S extends ElementStateHolder<S>>
    extends Self<S> {

    /**
     * Returns the state of the UI element.
     *
     * @return the state of the UI element
     */
    ElementState state();

    default boolean hovered() {
        return state().hovered;
    }

    default S hovered(boolean hovered) {
        state().hovered = hovered;
        return self();
    }

    default boolean disabled() {
        return state().disabled;
    }

    default S disabled(boolean disabled) {
        state().disabled = disabled;
        return self();
    }

    default boolean focused() {
        return state().focused;
    }

    default S focused(boolean focused) {
        state().focused = focused;
        return self();
    }
}
