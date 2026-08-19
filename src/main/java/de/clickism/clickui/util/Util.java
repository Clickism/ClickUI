package de.clickism.clickui.util;

import de.clickism.clickui.Element;
import de.clickism.clickui.UiScreen;
import de.clickism.clickui.layout.LayoutAxis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.ToIntFunction;

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

    /**
     * Returns the current UiScreen if the current screen is an instance of UiScreen, otherwise returns null.
     *
     * @return the current UiScreen or null if the current screen is not a UiScreen
     */
    public static @Nullable UiScreen currentUiScreen() {
        var screen = Minecraft.getInstance().screen;
        if (screen instanceof UiScreen uiScreen) {
            return uiScreen;
        }
        return null;
    }

    /**
     * Calculates the total gap between the children of the given element based on its child gap and number of children.
     *
     * @param element the element to calculate the total child gap for
     * @return the total gap between the children of the element
     */
    public static int totalChildGap(Element<?> element) {
        return element.childGap() * Math.max(0, element.children().size() - 1);
    }

    /**
     * Returns a function that retrieves the axis size of an element based on the given layout axis.
     *
     * @param axis the layout axis to determine the axis size
     * @return a function that retrieves the axis size of an element
     */
    public static ToIntFunction<Element<?>> axisGetter(LayoutAxis axis) {
        return axis.isHorizontal()
               ? child -> child.bounds().width()
               : child -> child.bounds().height();
    }
}
