package de.clickism.clickui.style;

import de.clickism.clickui.Element;
import de.clickism.clickui.state.State;
import de.clickism.clickui.state.StateHolder;

public record StyleContext(
    Element<?> element,
    State state
) implements StateHolder<StyleContext> {
}
