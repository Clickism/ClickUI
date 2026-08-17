package de.clickism.clickui.event;

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
}
