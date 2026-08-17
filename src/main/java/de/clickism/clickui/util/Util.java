package de.clickism.clickui.util;

import de.clickism.clickui.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Consumer;

public class Util {
    public static void postOrder(Element<?> element, Consumer<Element<?>> consumer) {
        for (var child : element.children()) {
            postOrder(child, consumer);
        }
        consumer.accept(element);
    }

    public static void preOrder(Element<?> element, Consumer<Element<?>> consumer) {
        consumer.accept(element);
        for (var child : element.children()) {
            preOrder(child, consumer);
        }
    }

    public static Font font() {
        return Minecraft.getInstance().font;
    }

    /**
     * Plays the sound for when a button is clicked.
     */
    public static void playDownSound() {
        var soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
