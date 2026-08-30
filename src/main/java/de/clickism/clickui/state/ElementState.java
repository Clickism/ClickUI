package de.clickism.clickui.state;

import de.clickism.clickui.UiElement;

public class ElementState implements ElementStateHolder<ElementState> {
    final UiElement<?> element;

    boolean hovered = false;
    boolean focused = false;
    boolean disabled = false;

    public ElementState(UiElement<?> element) {
        this.element = element;
    }

    @Override
    public ElementState state() {
        return this;
    }
}
