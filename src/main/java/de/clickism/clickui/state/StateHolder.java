package de.clickism.clickui.state;

import de.clickism.clickui.util.Self;

public interface StateHolder<S extends StateHolder<S>>
    extends Self<S> {

    /**
     * Returns the state of the UI element.
     *
     * @return the state of the UI element
     */
    State state();

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
}
