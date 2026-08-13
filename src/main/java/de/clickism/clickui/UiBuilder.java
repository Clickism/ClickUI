package de.clickism.clickui;

public interface UiBuilder {
    default Element box() {
        return new Element();
    }
}
