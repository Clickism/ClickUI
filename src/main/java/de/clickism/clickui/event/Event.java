package de.clickism.clickui.event;

import de.clickism.clickui.UiScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Generic interface representing en event.
 */
// TODO: Add element() method to get the UiElement that triggered the event
public interface Event {
    /**
     * Returns the state of the event.
     *
     * @return The state of the event.
     */
    EventState state();

    /**
     * Consumes the event, preventing further processing by other listeners.
     */
    default void consume() {
        state().consume();
    }

    /**
     * Returns the player associated with this event.
     *
     * @return The player associated with this event.
     */
    default LocalPlayer player() {
        return Minecraft.getInstance().player;
    }

    /**
     * Returns the currently active UiScreen.
     *
     * @return The currently active UiScreen.
     */
    default UiScreenHandler screen() {
        var screen = UiScreenHandler.current();
        if (screen == null) {
            throw new IllegalStateException("No UiScreen is currently active.");
        }
        return screen;
    }
}
