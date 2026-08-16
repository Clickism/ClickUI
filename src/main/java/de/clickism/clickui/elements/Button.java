package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.RenderContext;
import de.clickism.clickui.Size;
import de.clickism.clickui.Util;
import net.minecraft.network.chat.Component;

public class Button extends Element<Button> {
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
//        graphics.fill(this.bounds().x(), this.bounds().y(), this.bounds().x() + this.bounds().width(), this.bounds().y() + this.bounds().height(), 0xFF0000FF);
//        graphics.drawString(Util.font(), label, this.bounds().x() + 5, this.bounds().y() + 5, 0xFFFFFFFF);

        var button = net.minecraft.client.gui.components.Button.builder(label, net.minecraft.client.gui.components.Button::onPress)
            .bounds(this.bounds().x(), this.bounds().y(), this.bounds().width(), this.bounds().height())
            .build();
        button.render(graphics, context.mouseX(), context.mouseY(), context.delta());

//        // Yoinked from button
//        var bounds = this.bounds();
//        var alpha = 1.0f;
//
//        graphics.setColor(1.0F, 1.0F, 1.0F, alpha);
//        RenderSystem.enableBlend();
//        RenderSystem.enableDepthTest();
//        graphics.blitNineSliced(WIDGETS_LOCATION, bounds.x(), bounds.y(), bounds.width(), bounds.height(), 20, 4, 200, 20, 0, 66);
//        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
//        int k = 16777215;
//        graphics.drawString(Util.font(), label, bounds.x() + 5, bounds.y() + 5, k | Mth.ceil(alpha * 255.0F) << 24);
    }
}
