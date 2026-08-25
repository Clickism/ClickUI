package de.clickism.clickui.util;

import de.clickism.clickui.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Utility class for common UI operations.
 */
public class Util {
    private Util() {
        // Static class
    }

    /**
     * Traverses the element tree in post-order and applies the given consumer to each element.
     *
     * @param element  the root element to start the traversal from
     * @param consumer the consumer to apply to each element
     */
    public static void postOrder(Element<?> element, Consumer<Element<?>> consumer) {
        for (var child : element.children()) {
            postOrder(child, consumer);
        }
        consumer.accept(element);
    }

    /**
     * Traverses the element tree in pre-order and applies the given consumer to each element.
     *
     * @param element  the root element to start the traversal from
     * @param consumer the consumer to apply to each element
     */
    public static void preOrder(Element<?> element, Consumer<Element<?>> consumer) {
        consumer.accept(element);
        for (var child : element.children()) {
            preOrder(child, consumer);
        }
    }

    /**
     * Returns the font renderer used by the Minecraft client.
     *
     * @return the font renderer
     */
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

    /**
     * Opens a new screen in the Minecraft client.
     *
     * @param screen the screen to open
     */
    public static void openScreen(@Nullable Screen screen) {
        var minecraft = Minecraft.getInstance();
        minecraft.execute(() -> minecraft.setScreen(screen));
    }

}
