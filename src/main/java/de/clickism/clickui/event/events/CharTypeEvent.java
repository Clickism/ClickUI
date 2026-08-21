package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

// TODO: Rename to make more sense
public record CharTypeEvent(
    char character,
    int modifiers
) implements Event {
}
