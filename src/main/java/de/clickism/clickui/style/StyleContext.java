package de.clickism.clickui.style;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.state.ElementState;
import de.clickism.clickui.state.ElementStateHolder;

public record StyleContext(
    UiElement<?> element,
    ElementState state
) implements ElementStateHolder<StyleContext> {
}
