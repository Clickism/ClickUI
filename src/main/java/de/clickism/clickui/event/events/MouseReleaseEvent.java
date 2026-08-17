package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record MouseReleaseEvent(
    int x,
    int y,
    int button
) implements Event {
}
