package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record MouseScrollEvent(
    int x,
    int y,
    double delta
) implements Event {
}
