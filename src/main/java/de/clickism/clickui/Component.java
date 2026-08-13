package de.clickism.clickui;

/**
 * A component that can be built into a UI element.
 */
public interface Component extends UiBuilder {
    /**
     * Builds the element represented by this component.
     *
     * @return the built element
     */
    Element build();
}
