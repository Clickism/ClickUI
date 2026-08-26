package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;

public record DragStartEvent(
    double startX,
    double startY,
    int button,
    EventState state
) implements Event {
}
