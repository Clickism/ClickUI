package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record KeyReleaseEvent(
    int code,
    int scanCode,
    int modifiers,
    EventState state
) implements Event {
}
