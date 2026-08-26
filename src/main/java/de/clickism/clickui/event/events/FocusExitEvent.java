package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record FocusExitEvent(
    int x,
    int y,
    EventState state
) implements Event {
}
