package de.clickism.clickui.state;

import de.clickism.clickui.Self;

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
}
