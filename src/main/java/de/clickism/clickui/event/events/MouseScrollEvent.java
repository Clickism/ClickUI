package de.clickism.clickui.event.events;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record MouseScrollEvent(
    UiElement<?> element,
    int x,
    int y,
    double delta,
    EventState state
) implements Event {
}
