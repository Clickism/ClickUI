package de.clickism.clickui.event.events;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;
import de.clickism.clickui.util.Util;

public record MouseClickEvent(
    UiElement<?> element,
    int x,
    int y,
    int button,
    EventState state
) implements Event, MouseButtonEvent {
    /**
     * Plays a sound effect for the mouse click event.
     */
    public void playSound() {
        Util.playDownSound();
    }
}
