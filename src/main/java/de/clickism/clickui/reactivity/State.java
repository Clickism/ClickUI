package de.clickism.clickui.reactivity;

import de.clickism.clickui.Component;

import java.util.function.Function;

public class State<T> {
    private T value;
    private final Component<?> parent;

    public State(T value, Component<?> parent) {
        this.value = value;
        this.parent = parent;
    }

    public T get() {
        return value;
    }

    public void update(T value) {
        this.value = value;
        this.parent.rebuild();
    }

    public void update(Function<T, T> updater) {
        this.value = updater.apply(this.value);
        this.parent.rebuild();
    }
}
