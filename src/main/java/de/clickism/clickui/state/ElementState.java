package de.clickism.clickui.state;

public class ElementState implements ElementStateHolder<ElementState> {
    boolean hovered = false;

    @Override
    public ElementState state() {
        return this;
    }
}
