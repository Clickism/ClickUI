package de.clickism.clickui;

import de.clickism.clickui.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface UiScreenControls {
    /**
     * Returns the underlying Minecraft screen associated with this UiScreen.
     *
     * @return the underlying Minecraft screen
     */
    Screen screenToOpen();

    /**
     * Opens this UiScreen in the Minecraft client, setting the current screen as its parent.
     * <p>
     * Will navigate back to the previous screen when this screen is closed.
     */
    default void open() {
        open(Util.currentScreen());
    }

    /**
     * Opens this UiScreen in the Minecraft client, setting the specified parent screen.
     *
     * @param parent the parent screen to set for this UiScreen
     */
    default void open(@Nullable Screen parent) {
        var screen = screenToOpen();

        if (screen instanceof UiScreenHandler handler) {
            handler.parentScreen(parent);
        }

        Util.openScreen(screen);
    }

    /**
     * Opens this UiScreen in the Minecraft client without setting a parent screen.
     * <p>
     * Will close all other screens when this screen is closed.
     */
    default void openFresh() {
        open(null);
    }

    /**
     * Closes this screen and navigates back to the parent screen, if any.
     */
    default void close() {
        var current = UiScreenHandler.current();

        if (current == null) {
            Util.openScreen(null);
            return;
        }

        var parent = current.parentScreen();
        Util.openScreen(parent);
    }

    /**
     * Closes all open screens.
     */
    default void closeAll() {
        Util.openScreen(null);
    }
}
