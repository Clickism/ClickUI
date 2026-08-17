package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record MouseExitEvent(
    int x,
    int y
) implements Event {
}
