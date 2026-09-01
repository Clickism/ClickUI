package de.clickism.clickui.event.events;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;
import de.clickism.clickui.util.Util;

public record MouseClickEvent(
    int x,
    int y,
    int button,
    EventState state
) implements Event {
    /**
     * Plays a sound effect for the mouse click event.
     */
    public void playSound() {
        Util.playDownSound();
    }

    /**
     * Checks if the mouse click event is a left click.
     *
     * @return true if the event is a left click, false otherwise
     */
    public boolean isLeftClick() {
        return button == 0;
    }

    /**
     * Checks if the mouse click event is a right click.
     *
     * @return true if the event is a right click, false otherwise
     */
    public boolean isRightClick() {
        return button == 1;
    }

    /**
     * Checks if the mouse click event is a middle click.
     *
     * @return true if the event is a middle click, false otherwise
     */
    public boolean isMiddleClick() {
        return button == 2;
    }
}
