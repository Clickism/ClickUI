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
     * Returns the parent screen of this UiScreen, if any.
     *
     * @return the parent screen, or null if there is no parent
     */
    default @Nullable Screen parentScreen() {
        var current = UiScreenHandler.current();
        if (current != null) {
            return current.parentScreen();
        }
        return null;
    }

    /**
     * Sets the parent screen of this UiScreen.
     *
     * @param screen the parent screen to set, or null if there is no parent
     */
    default void parentScreen(@Nullable Screen screen) {
        var current = UiScreenHandler.current();
        if (current != null) {
            current.parentScreen(screen);
        }
    }

    /**
     * Opens this UiScreen in the Minecraft client, setting the current screen as its parent.
     * <p>
     * Will navigate back to the previous screen when this screen is closed.
     */
    default void open() {
        open(Minecraft.getInstance().screen);
    }

    /**
     * Opens this UiScreen in the Minecraft client, setting the specified parent screen.
     *
     * @param parent the parent screen to set for this UiScreen
     */
    default void open(@Nullable Screen parent) {
        Util.openScreen(this.screenToOpen());
        this.parentScreen(parent);
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
        var parent = parentScreen();
        if (parent == null) {
            Util.openScreen(null);
            return;
        }
        Util.openScreen(parent);
    }

    /**
     * Closes all open screens.
     */
    default void closeAll() {
        Util.openScreen(null);
    }
}
