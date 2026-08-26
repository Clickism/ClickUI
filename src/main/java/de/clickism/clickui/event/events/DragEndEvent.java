package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record DragEndEvent(
    double startX,
    double startY,
    double endX,
    double endY,
    int button,
    EventState state
) implements Event {
}
