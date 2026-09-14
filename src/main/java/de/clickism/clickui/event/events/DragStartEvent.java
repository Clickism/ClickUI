package de.clickism.clickui.event.events;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record DragStartEvent(
    UiElement<?> element,
    double startX,
    double startY,
    int button,
    EventState state
) implements Event {
}
