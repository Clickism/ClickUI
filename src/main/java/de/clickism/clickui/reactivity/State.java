package de.clickism.clickui.reactivity;

import java.util.function.Function;

public class State<T> {
    private T value;
    private final Runnable callback;

    public State(T value, Runnable callback) {
        this.value = value;
        this.callback = callback;
    }

    public T get() {
        return value;
    }

    public void update(T value) {
        this.value = value;
        this.callback.run();
    }

    public void update(Function<T, T> updater) {
        this.value = updater.apply(this.value);
        this.callback.run();
    }
}
