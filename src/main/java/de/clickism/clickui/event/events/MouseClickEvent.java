package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record MouseClickEvent(
    int x,
    int y,
    int button
) implements Event {
}
