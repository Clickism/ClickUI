package de.clickism.clickui.state;

public class State implements StateHolder<State> {
    boolean hovered = false;
    boolean disabled = false;

    @Override
    public State state() {
        return this;
    }
}
