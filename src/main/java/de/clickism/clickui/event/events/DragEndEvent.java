package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record DragEndEvent(
    double startX,
    double startY,
    double endX,
    double endY,
    int button
) implements Event {
}
