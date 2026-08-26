package de.clickism.clickui.state;

import de.clickism.clickui.Element;

public class ElementState implements ElementStateHolder<ElementState> {
    final Element<?> element;

    boolean hovered = false;
    boolean focused = false;
    boolean disabled = false;

    public ElementState(Element<?> element) {
        this.element = element;
    }

    @Override
    public ElementState state() {
        return this;
    }
}
