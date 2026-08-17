package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.render.ScaledTextRenderer;
import de.clickism.clickui.util.Util;
import net.minecraft.network.chat.Component;

// TODO: Text alignment and wrapping
public class Text extends Element<Text> {
    private Component text;

    public Text(Component text) {
        this.text = text;
    }

    public void text(Component text) {
        this.text = text;
        this.invalidate();
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
        // TODO: Text color?
        renderer.render(text, x, y, style.fontScale(), 0xFFFFFFFF);
    }
}
