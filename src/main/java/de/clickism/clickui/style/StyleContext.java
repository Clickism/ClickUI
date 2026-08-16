package de.clickism.clickui.style;

import de.clickism.clickui.Element;
import de.clickism.clickui.state.ElementState;
import de.clickism.clickui.state.ElementStateHolder;

public record StyleContext(
    Element<?> element,
    ElementState state
) implements ElementStateHolder<StyleContext> {
}
