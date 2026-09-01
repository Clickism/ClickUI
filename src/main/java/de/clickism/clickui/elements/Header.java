package de.clickism.clickui.elements;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/**
 * A simple UI element that represents a header with a title and level.
 */
public class Header extends Text {
    /**
     * Creates a new Header element with the specified title and level.
     *
     * @param text  The title of the header.
     * @param level The level of the header (1-5), which determines the font size and padding.
     */
    public Header(Component text, int level) {
        super(text.copy().withStyle(ChatFormatting.BOLD));
        var style = this.elementStyle();
        switch (level) {
            // TODO: Refactor padding to be more visible
            case 1 -> {
                style.fontScale(2f);
                padding(6, 0);
            }
            case 2 -> {
                style.fontScale(1.5f);
                padding(4, 0);
            }
            case 3 -> {
                style.fontScale(1.17f);
                padding(2, 0);
            }
            case 4 -> {
                style.fontScale(1f);
                padding(2, 0);
            }
            case 5 -> {
                style.fontScale(0.83f);
                padding(2, 0);
            }
        }
    }
}
