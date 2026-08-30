package de.clickism.clickui.reactivity;

import de.clickism.clickui.UiComponent;

import java.util.function.Function;

public class State<T> {
    private T value;
    private final UiComponent<?> parent;

    public State(T value, UiComponent<?> parent) {
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
