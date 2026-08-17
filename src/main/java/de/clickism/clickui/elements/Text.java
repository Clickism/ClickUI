package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.layout.Size;
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
        return new Size(Util.font().width(text), Util.font().lineHeight);
    }

    @Override
    public void render(RenderContext context) {
        var graphics = context.graphics();
        // Align text within padding and bounds
        var x = this.bounds().x() + this.padding().left();
        var y = this.bounds().y() + this.padding().top();
        graphics.drawString(Util.font(), text, x, y, 0xFFFFFFFF);
    }
}
