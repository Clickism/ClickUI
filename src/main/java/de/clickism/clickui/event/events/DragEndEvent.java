package de.clickism.clickui.event.events;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record DragEndEvent(
    UiElement<?> element,
    double startX,
    double startY,
    double endX,
    double endY,
    int button,
    EventState state
) implements Event {
}
