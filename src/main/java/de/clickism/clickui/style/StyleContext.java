package de.clickism.clickui.style;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.state.ElementState;
import de.clickism.clickui.state.ElementStateHolder;

/**
 * Represents the context in which a style is applied to a UI element. It contains the element, its current state, and the screen style.
 *
 * @param element the UI element for which the style is being resolved
 * @param state   the current state of the element for which the style is being resolved
 */
public record StyleContext(
    UiElement<?> element,
    ElementState state
) implements ElementStateHolder<StyleContext> {
    public StyleContext of(UiElement<?> element, StyleMap screenStyle) {
        return new StyleContext(element, element.state());
    }
}
