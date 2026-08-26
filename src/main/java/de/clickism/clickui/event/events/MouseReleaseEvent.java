package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record MouseReleaseEvent(
    int x,
    int y,
    int button,
    EventState state
) implements Event {
}
