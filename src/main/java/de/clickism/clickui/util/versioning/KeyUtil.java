package de.clickism.clickui.util.versioning;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

//? if >=26.3 {
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.input.InputQuirks;
import org.lwjgl.sdl.SDLKeyboard;
//?} else
//import org.lwjgl.glfw.GLFW;

//~ if >= 26.1 'Screen' -> 'Minecraft.getInstance()' {
public class KeyUtil {
    //? if >=26.3 {
    public static final int KEY_BACKSPACE = InputConstants.KEY_BACKSPACE;
    public static final int KEY_DELETE = InputConstants.KEY_DELETE;
    public static final int KEY_LEFT = InputConstants.KEY_LEFT;
    public static final int KEY_RIGHT = InputConstants.KEY_RIGHT;
    public static final int KEY_UP = InputConstants.KEY_UP;
    public static final int KEY_DOWN = InputConstants.KEY_DOWN;
    public static final int KEY_HOME = InputConstants.KEY_HOME;
    public static final int KEY_END = InputConstants.KEY_END;
    public static final int KEY_TAB = InputConstants.KEY_TAB;
    public static final int KEY_ENTER = InputConstants.KEY_RETURN;
    public static final int KEY_KP_ENTER = InputConstants.KEY_NUMPADENTER;
    //?} else {
    /*public static final int KEY_BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    public static final int KEY_DELETE = GLFW.GLFW_KEY_DELETE;
    public static final int KEY_LEFT = GLFW.GLFW_KEY_LEFT;
    public static final int KEY_RIGHT = GLFW.GLFW_KEY_RIGHT;
    public static final int KEY_UP = GLFW.GLFW_KEY_UP;
    public static final int KEY_DOWN = GLFW.GLFW_KEY_DOWN;
    public static final int KEY_HOME = GLFW.GLFW_KEY_HOME;
    public static final int KEY_END = GLFW.GLFW_KEY_END;
    public static final int KEY_TAB = GLFW.GLFW_KEY_TAB;
    public static final int KEY_ENTER = GLFW.GLFW_KEY_ENTER;
    public static final int KEY_KP_ENTER = GLFW.GLFW_KEY_KP_ENTER;
    *///?}

    public static boolean hasShiftDown() {
        return Minecraft.getInstance().hasShiftDown();
    }

    public static boolean hasControlDown() {
        //? if >=26.3 {
        return (SDLKeyboard.SDL_GetModState() & InputQuirks.EDIT_SHORTCUT_KEY_MODIFIER) != 0;
        //?} else
        //return Minecraft.getInstance().hasControlDown();
    }
}
//~}
