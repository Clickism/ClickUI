package de.clickism.clickui;

public class Ref<T> {
    private T value;

    public Ref() {
        this.value = null;
    }

    public T get() {
        if (value == null) {
            throw new IllegalStateException("Ref value is not set");
        }
        return value;
    }

    public T getOrNull() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
