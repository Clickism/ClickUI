package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record DragEvent(
    double startX,
    double startY,
    double currentX,
    double currentY,
    double deltaX,
    double deltaY,
    int button
) implements Event {
}
