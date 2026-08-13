package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.RenderContext;
import de.clickism.clickui.Size;
import de.clickism.clickui.Util;
import net.minecraft.network.chat.Component;

public class Button extends Element {
    private Component label;

    public Button(Component label) {
        this.label = label;
    }

    public void label(Component label) {
        this.label = label;
        this.invalidate();
    }

    @Override
    public Size intrinsicSize() {
        return new Size(Util.font().width(label) + 10, Util.font().lineHeight + 10);
    }

    @Override
    public void render(RenderContext context) {
        var graphics = context.graphics();
        graphics.fill(this.bounds().x(), this.bounds().y(), this.bounds().x() + this.bounds().width(), this.bounds().y() + this.bounds().height(), 0xFF0000FF);
        graphics.drawString(Util.font(), label, this.bounds().x() + 5, this.bounds().y() + 5, 0xFFFFFFFF);
    }
}
