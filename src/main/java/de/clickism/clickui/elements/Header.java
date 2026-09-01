package de.clickism.clickui.elements;

import de.clickism.clickui.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple UI element that represents a header with a title and level.
 */
public class Header extends Text {
    private final Component text;
    private Component styledText;

    private Set<ChatFormatting> styles = new HashSet<>();

    /**
     * Creates a new Header element with the specified title and level.
     *
     * @param text  The title of the header.
     * @param level The level of the header (1-5), which determines the font size and padding.
     */
    public Header(Component text, int level) {
        super(text);
        this.text = text;
        float fontScale;
        switch (level) {
            // TODO: Refactor padding to be more visible
            case 1 -> {
                fontScale = 2.0f;
                bold(true);
                padding(em(0.67f), 0);
            }
            case 2 -> {
                fontScale = 1.5f;
                bold(true);
                padding(em(0.83f), 0);
            }
            case 3 -> {
                fontScale = 1.17f;
                bold(true);
                padding(em(1f), 0);
            }
            case 4 -> {
                fontScale = 1.0f;
                bold(true);
                padding(em(1.33f), 0);
            }
            case 5 -> {
                fontScale = 1.0f;
                underline(true);
                color(ChatFormatting.GRAY);
                padding(em(1.33f), 0);
            }
            case 6 -> {
                fontScale = 0.83f;
                underline(true);
                color(ChatFormatting.GRAY);
                padding(em(1.67f), 0);
            }
            default -> {
                throw new IllegalArgumentException("Header level must be between 1 and 6");
            }
        }
        this.style(style().fontScale(fontScale));
    }

    private int em(float multiplier) {
        return (int) (Util.font().lineHeight * multiplier);
    }

    public Header bold(boolean bold) {
        if (bold) {
            styles.add(ChatFormatting.BOLD);
        } else {
            styles.remove(ChatFormatting.BOLD);
        }
        updateStyledText();
        return this;
    }

    public Header underline(boolean underline) {
        if (underline) {
            styles.add(ChatFormatting.UNDERLINE);
        } else {
            styles.remove(ChatFormatting.UNDERLINE);
        }
        updateStyledText();
        return this;
    }

    public Header color(ChatFormatting color) {
        styles.add(color);
        updateStyledText();
        return this;
    }

    private void updateStyledText() {
        this.styledText = text.copy().withStyle(styles.toArray(new ChatFormatting[0]));
        super.text(styledText);
    }
}
