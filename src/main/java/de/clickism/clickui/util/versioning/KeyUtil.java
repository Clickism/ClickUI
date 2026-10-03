package de.clickism.clickui.util.versioning;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

//? if >=26.3 {
import org.lwjgl.sdl.SDLKeycode;
//?} else
//import org.lwjgl.glfw.GLFW;

//~ if >= 26.1 'Screen' -> 'Minecraft.getInstance()' {
public class KeyUtil {
    //? if >=26.3 {
    public static final int KEY_BACKSPACE = SDLKeycode.SDLK_BACKSPACE;
    public static final int KEY_DELETE = SDLKeycode.SDLK_DELETE;
    public static final int KEY_LEFT = SDLKeycode.SDLK_LEFT;
    public static final int KEY_RIGHT = SDLKeycode.SDLK_RIGHT;
    public static final int KEY_UP = SDLKeycode.SDLK_UP;
    public static final int KEY_DOWN = SDLKeycode.SDLK_DOWN;
    public static final int KEY_HOME = SDLKeycode.SDLK_HOME;
    public static final int KEY_END = SDLKeycode.SDLK_END;
    public static final int KEY_TAB = SDLKeycode.SDLK_TAB;
    public static final int KEY_ENTER = SDLKeycode.SDLK_RETURN;
    public static final int KEY_KP_ENTER = SDLKeycode.SDLK_KP_ENTER;
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
        return Minecraft.getInstance().hasControlDown();
    }
}
//~}
