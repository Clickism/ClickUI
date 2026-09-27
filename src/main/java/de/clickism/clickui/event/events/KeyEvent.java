package de.clickism.clickui.event.events;

import net.minecraft.client.gui.screens.Screen;

public interface KeyEvent {
    int code();
    int scanCode();
    int modifiers();

    //? if >= 26.1 {
    /*/^*
     * Converts this KeyPressEvent to a Minecraft KeyEvent.
     *
     * @return a new KeyEvent instance with the same key code, scan code, and modifiers
     ^/
    default net.minecraft.client.input.KeyEvent asKeyEvent() {
        return new net.minecraft.client.input.KeyEvent(code(), scanCode(), modifiers());
    }
    *///?}

    default boolean isSelectAll() {
        //? if >= 26.1 {
        /*return asKeyEvent().isSelectAll();
        *///?} else
        return Screen.isSelectAll(code());
    }

    default boolean isCopy() {
        //? if >= 26.1 {
        /*return asKeyEvent().isCopy();
        *///?} else
        return Screen.isCopy(code());
    }

    default boolean isCut() {
        //? if >= 26.1 {
        /*return asKeyEvent().isCut();
        *///?} else
        return Screen.isCut(code());
    }

    default boolean isPaste() {
        //? if >= 26.1 {
        /*return asKeyEvent().isPaste();
        *///?} else
        return Screen.isPaste(code());
    }
}
