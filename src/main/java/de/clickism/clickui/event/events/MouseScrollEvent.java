package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record MouseScrollEvent(
    int x,
    int y,
    double delta,
    EventState state
) implements Event {
}
