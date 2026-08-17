package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;

public record KeyPressEvent(
    int keyCode,
    int scanCode,
    int modifiers
) implements Event {
}
