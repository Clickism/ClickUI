package de.clickism.clickui.event.events;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

// TODO: Rename to make more sense
public record CharTypeEvent(
    UiElement<?> element,
    char character,
    int modifiers,
    EventState state
) implements Event {
}
