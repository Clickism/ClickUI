package de.clickism.clickui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.util.function.Consumer;

public class Util {
    public static void postOrder(Element element, Consumer<Element> consumer) {
        for (Element child : element.children()) {
            postOrder(child, consumer);
        }
        consumer.accept(element);
    }

    public static void preOrder(Element element, Consumer<Element> consumer) {
        consumer.accept(element);
        for (Element child : element.children()) {
            preOrder(child, consumer);
        }
    }

    public static Font font() {
        return Minecraft.getInstance().font;
    }
}
