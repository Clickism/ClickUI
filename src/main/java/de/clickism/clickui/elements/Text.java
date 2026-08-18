package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.render.ScaledTextRenderer;
import de.clickism.clickui.util.Util;
import net.minecraft.network.chat.Component;

// TODO: Text alignment and wrapping

/**
 * A simple UI element used for displaying text.
 */
public class Text extends Element<Text> {
    /**
     * The text to displaY.
     */
    private Component text;
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
        this.text = text;
    }

    /**
     * Sets the text of the Text element.
     *
     * @param text The text to display.
     * @return The current Text element instance.
     */
    public Text text(Component text) {
        this.text = text;
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
    public Text textAlign(Align align) {
        this.align = align;
        this.invalidate();
        return this;
    }

    /**
     * Sets the text alignment of the Text element to left.
     *
     * @return The current Text element instance.
     */
    public Text textAlignLeft() {
        return textAlign(Align.LEFT);
    }

    /**
     * Sets the text alignment of the Text element to center.
     *
     * @return The current Text element instance.
     */
    public Text textAlignCenter() {
        return textAlign(Align.CENTER);
    }

    /**
     * Sets the text alignment of the Text element to right.
     *
     * @return The current Text element instance.
     */
    public Text textAlignRight() {
        return textAlign(Align.RIGHT);
    }

    @Override
    public Size intrinsicSize() {
        // TODO: Font size, multiline, etc.
        var fontScale = this.resolvedStyle().fontScale();
        var width = Util.font().width(text) * fontScale;
        var height = Util.font().lineHeight * fontScale;
        // Ceil the size to ensure it fits within the bounds
        return new Size((int) Math.ceil(width), (int) Math.ceil(height));
    }

    @Override
    public void render(RenderContext context) {
        // Align text within padding and bounds
        var x = this.bounds().x() + this.padding().left();
        var y = this.bounds().y() + this.padding().top();
        var renderer = new ScaledTextRenderer(context);
        var style = this.resolvedStyle();
        // TODO: Cascading text color?
        // TODO: Render alignment
        // TODO: Text color?
        renderer.render(text, x, y, style.fontScale(), 0xFFFFFFFF);
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
