package de.clickism.clickui.elements;

import com.google.common.util.concurrent.AtomicDouble;
import de.clickism.clickui.Element;
import de.clickism.clickui.Wrappable;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.render.ScaledTextRenderer;
import de.clickism.clickui.util.Util;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

import java.util.ArrayList;
import java.util.List;

// TODO: Text alignment and wrapping

/**
 * A simple UI element used for displaying text.
 */
public class Text extends Element<Text> implements Wrappable {
    /**
     * The text to displaY.
     */
    private Component text;
    private List<FormattedText> lines;
    /**
     * The alignment of the text within.
     */
    private Align align = Align.LEFT;

    /**
     * Creates a new Text element with the specified text.
     *
     * @param text The text to display.
     */
    public Text(Component text) {
        this.text(text);
    }

    /**
     * Sets the text of the Text element.
     *
     * @param text The text to display.
     * @return The current Text element instance.
     */
    public Text text(Component text) {
        this.text = text;
        this.lines = Util.font()
            .getSplitter()
            .splitLines(text, Integer.MAX_VALUE, Style.EMPTY);
        this.invalidate();
        return this;
    }

    /**
     * Sets the text of the Text element using a String.
     *
     * @param text The text to display.
     * @return The current Text element instance.
     */
    public Text text(String text) {
        this.text(Component.literal(text));
        return this;
    }

    /**
     * Sets the text alignment of the Text element.
     *
     * @param align The alignment to set for the text.
     * @return The current Text element instance.
     */
    public Text alignText(Align align) {
        this.align = align;
        this.invalidate();
        return this;
    }

    /**
     * Sets the text alignment of the Text element to left.
     *
     * @return The current Text element instance.
     */
    public Text alignTextLeft() {
        return alignText(Align.LEFT);
    }

    /**
     * Sets the text alignment of the Text element to center.
     *
     * @return The current Text element instance.
     */
    public Text alignTextCenter() {
        return alignText(Align.CENTER);
    }

    /**
     * Sets the text alignment of the Text element to right.
     *
     * @return The current Text element instance.
     */
    public Text alignTextRight() {
        return alignText(Align.RIGHT);
    }

    @Override
    public Size intrinsicSize() {
        var fontScale = this.resolvedStyle().fontScale();
        var width = lines.stream()
            .mapToDouble(line -> Util.font().width(line))
            .max()
            .orElse(0) * fontScale;
        var height = Util.font().lineHeight * fontScale * lines.size();
        // Ceil the size to ensure it fits within the bounds
        return new Size((int) Math.ceil(width), (int) Math.ceil(height));
    }

    @Override
    public Size defaultMinSize() {
        var splitter = Util.font().getSplitter();
        var scale = resolvedStyle().fontScale();

        final var currentWord = new ArrayList<FormattedText>();
        final var maxWordWidth = new AtomicDouble(0);

        // Iterate over the text and find the maximum word widthx
        StringDecomposer.iterateFormatted(text, Style.EMPTY, (index, style, codePoint) -> {
            if (codePoint == ' ' || codePoint == '\n') {
                if (!currentWord.isEmpty()) {
                    var word = FormattedText.composite(currentWord);
                    maxWordWidth.getAndUpdate(value ->
                        Math.max(value, splitter.stringWidth(word)));
                    currentWord.clear();
                }
                return true;
            }
            currentWord.add(FormattedText.of(
                new String(Character.toChars(codePoint)),
                style
            ));
            return true;
        });

        // Handle the last word if the text doesn't end with a space or newline
        if (!currentWord.isEmpty()) {
            var word = FormattedText.composite(currentWord);
            maxWordWidth.getAndUpdate(value ->
                Math.max(value, splitter.stringWidth(word)));
        }

        var height = Util.font().lineHeight * scale;

        var minWidth = (int) Math.ceil(maxWordWidth.get() * scale);
        var minHeight = (int) Math.ceil(height);

        // Add padding to the minimum size
        minWidth += this.padding().horizontal();
        minHeight += this.padding().vertical();

        return new Size(minWidth, minHeight);
    }

    @Override
    public void wrap(int maxWidth) {
        var font = Util.font();
        this.lines = font
            .getSplitter()
            .splitLines(text, maxWidth, Style.EMPTY);
        // Calculate new height
        var fontScale = this.resolvedStyle().fontScale();
        var height = font.lineHeight * fontScale * lines.size();
        height += this.padding().vertical();
        // Update bounds with new height
        this.bounds(this.bounds().withHeight((int) Math.ceil(height)));
    }

    @Override
    public void render(RenderContext context) {
        // Align text within padding and bounds
        var x = this.bounds().x() + this.padding().left();
        var y = this.bounds().y() + this.padding().top();
        var renderer = new ScaledTextRenderer(context);
        var style = this.resolvedStyle();

        // TODO: Cascading text color?
        // TODO: Text color?
        var charLines = Language.getInstance().getVisualOrder(lines);

        // Render each line
        for (var line : charLines) {
            // Align text based on the specified alignment
            var width = renderer.measureWidth(line, style.fontScale());
            var space = this.bounds().width() - this.padding().horizontal() - width;
            var lineX = x;
            switch (align) {
                case CENTER -> lineX = (int) (x + space / 2);
                case RIGHT -> lineX = (int) (x + space);
                case LEFT -> {
                    // No adjustment needed for left alignment
                }
            }
            // Render text
            renderer.render(line, lineX, y, style.fontScale(), 0xFFFFFFFF);
            y += (int) renderer.measureHeight(style.fontScale());
        }
    }

    /**
     * Text alignment enum.
     */
    public enum Align {
        LEFT,
        CENTER,
        RIGHT
    }
}
