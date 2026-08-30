package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record DragEvent(
    double startX,
    double startY,
    double currentX,
    double currentY,
    double deltaX,
    double deltaY,
    int button,
    EventState state
) implements Event {
    public double totalDeltaX() {
        return currentX - startX;
    }

    public double totalDeltaY() {
        return currentY - startY;
    }
}
