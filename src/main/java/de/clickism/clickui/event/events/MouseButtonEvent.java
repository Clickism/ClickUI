package de.clickism.clickui.event.events;

//? if >=26.3 {
import org.lwjgl.sdl.SDLMouse;
//?} else {
//import org.lwjgl.glfw.GLFW;
//?}

public interface MouseButtonEvent {
    //? if >=26.3 {
    int LEFT_BUTTON = SDLMouse.SDL_BUTTON_LEFT;
    int RIGHT_BUTTON = SDLMouse.SDL_BUTTON_RIGHT;
    int MIDDLE_BUTTON = SDLMouse.SDL_BUTTON_MIDDLE;
    //?} else {
    /*int LEFT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    int RIGHT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    int MIDDLE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
    *///?}

    int button();

    /**
     * Checks if the mouse event is a left click.
     *
     * @return true if the event is a left click, false otherwise
     */
    default boolean isLeftClick() {
        return button() == LEFT_BUTTON;
    }

    /**
     * Checks if the mouse event is a right click.
     *
     * @return true if the event is a right click, false otherwise
     */
    default boolean isRightClick() {
        return button() == RIGHT_BUTTON;
    }

    /**
     * Checks if the mouse event is a middle click.
     *
     * @return true if the event is a middle click, false otherwise
     */
    default boolean isMiddleClick() {
        return button() == MIDDLE_BUTTON;
    }
}
