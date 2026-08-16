package de.clickism.clickui.state;

public class State implements StateHolder<State> {
    boolean hovered = false;

    @Override
    public State state() {
        return this;
    }
}
