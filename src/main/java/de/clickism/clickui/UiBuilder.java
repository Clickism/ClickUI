package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import de.clickism.clickui.elements.Button;
import de.clickism.clickui.elements.Text;
import net.minecraft.network.chat.Component;

public interface UiBuilder {
    default Element box() {
        return new Box();
    }

    default Button button(Component label) {
        return new Button(label);
    }

    default Button button(String label) {
        return new Button(Component.literal(label));
    }

    default Text text(String text) {
        return new Text(Component.literal(text));
    }

    default Text text(Component text) {
        return new Text(text);
    }
}
