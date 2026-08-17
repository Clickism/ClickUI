package de.clickism.clickui.event;

import de.clickism.clickui.UiScreen;
import de.clickism.clickui.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Generic interface representing en event.
 */
public interface Event {
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
    default UiScreen screen() {
        var screen = Util.currentUiScreen();
        if (screen == null) {
            throw new IllegalStateException("No UiScreen is currently active.");
        }
        return screen;
    }
}
