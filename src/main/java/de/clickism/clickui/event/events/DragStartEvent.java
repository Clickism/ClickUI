package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record DragStartEvent(
    double startX,
    double startY,
    int button
) implements Event {
}
