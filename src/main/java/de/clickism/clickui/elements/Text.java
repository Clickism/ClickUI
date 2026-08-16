package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.RenderContext;
import de.clickism.clickui.Size;
import de.clickism.clickui.Util;
import net.minecraft.network.chat.Component;

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
        return new Size(Util.font().width(text), Util.font().lineHeight);
    }

    @Override
    public void render(RenderContext context) {
        var graphics = context.graphics();
        var x = this.bounds().x() + this.padding().left();
        var y = this.bounds().y() + this.padding().top();
        graphics.drawString(Util.font(), text, x, y, 0xFFFFFFFF);
    }
}
