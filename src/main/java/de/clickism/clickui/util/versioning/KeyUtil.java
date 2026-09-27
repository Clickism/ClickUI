package de.clickism.clickui.util.versioning;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

//~ if >= 26.1 'Screen' -> 'Minecraft.getInstance()' {
public class KeyUtil {
    public static boolean hasShiftDown() {
        return Minecraft.getInstance().hasShiftDown();
    }

    public static boolean hasControlDown() {
        return Minecraft.getInstance().hasControlDown();
    }
}
//~}
