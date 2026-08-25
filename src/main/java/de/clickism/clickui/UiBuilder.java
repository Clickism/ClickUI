package de.clickism.clickui;

import de.clickism.clickui.elements.*;
import de.clickism.clickui.elements.input.NumberField;
import de.clickism.clickui.elements.input.TextField;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface UiBuilder {
    default <T> Ref<T> ref() {
        return new Ref<>();
    }

    default Box box() {
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

    default Header h1(String title) {
        return new Header(Component.literal(title), 1);
    }

    default Header h1(Component title) {
        return new Header(title, 1);
    }

    default Header h2(String title) {
        return new Header(Component.literal(title), 2);
    }

    default Header h2(Component title) {
        return new Header(title, 2);
    }

    default Header h3(String title) {
        return new Header(Component.literal(title), 3);
    }

    default Header h3(Component title) {
        return new Header(title, 3);
    }

    default Header h4(String title) {
        return new Header(Component.literal(title), 4);
    }

    default Header h4(Component title) {
        return new Header(title, 4);
    }

    default Header h5(String title) {
        return new Header(Component.literal(title), 5);
    }

    default Header h5(Component title) {
        return new Header(title, 5);
    }

    default TextField textField() {
        return new TextField();
    }

    default TextField textField(String placeholder) {
        return new TextField()
            .placeholder(placeholder);
    }

    default NumberField numberField() {
        return new NumberField();
    }

    default NumberField numberField(String placeholder) {
        return new NumberField()
            .placeholder(placeholder);
    }

    default Image image(ResourceLocation location, int width, int height) {
        return new Image(location, width, height);
    }
}
