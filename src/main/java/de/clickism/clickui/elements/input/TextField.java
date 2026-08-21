package de.clickism.clickui.elements.input;

import de.clickism.clickui.layout.Padding;
import de.clickism.clickui.layout.Point;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.BorderPosition;
import de.clickism.clickui.util.Util;
import net.minecraft.client.renderer.RenderType;

import java.awt.*;

public class TextField extends AbstractTextField<TextField> {
    private static final Padding DEFAULT_PADDING = Padding.create(5);
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 20;

    public TextField() {
        // Set default props
        // TODO: Better default height
        this.padding(DEFAULT_PADDING);
        // Set style
        this.style(s -> s
            .background(Color.BLACK)
            .border(new Color(0xFFA0A0A0))
            .borderPosition(BorderPosition.INSIDE)
            .hovered(h -> h
                .border(Color.WHITE))
        );
    }

    @Override
    public Size intrinsicSize() {
        var height = DEFAULT_HEIGHT - DEFAULT_PADDING.vertical();
        return new Size(DEFAULT_WIDTH, height);
    }

    @Override
    protected void renderBackground(RenderContext context) {
        // Rendered via style
    }

    private Point textPosition() {
        var bounds = bounds();
        var x = bounds.x();
        var y = bounds.y();
        var padding = padding();
        // Align
        var textHeight = Util.font().lineHeight;
        y += (bounds.height() - textHeight) / 2;
        y += 1; // Better visual alignment
        // Add padding
        x += padding.left();
        return new Point(x, y);
    }

    @Override
    protected void renderText(RenderContext context) {
        var graphics = context.graphics();

        // Enable scissor
        var bounds = bounds();
        graphics.enableScissor(
            bounds.x(),
            bounds.y(),
            bounds.x() + bounds.width(),
            bounds.y() + bounds.height()
        );
        var textPos = textPosition();
        // Render texts
        graphics.drawString(Util.font(), value(), textPos.x(), textPos.y(), 0xFFFFFFFF);
        // Disable scissor
        graphics.disableScissor();
    }

    @Override
    protected void renderPlaceholder(RenderContext context) {

    }

    @Override
    protected void renderCursor(RenderContext context) {

    }

    @Override
    protected void renderHighlight(RenderContext context) {
        if (highlightPos == cursorPos) return;
        var graphics = context.graphics();

        int highlightStart = Math.min(cursorPos, highlightPos);
        int highlightEnd = Math.max(cursorPos, highlightPos);

        var textPos = textPosition();
        var x = textPos.x();
        var y = textPos.y();

        var font = Util.font();
        int highlightX = x + font.width(textToShow().substring(0, highlightStart));
        int highlightWidth = font.width(textToShow().substring(highlightStart, highlightEnd));

        // Render highlight rectangle
        graphics.fill(
            RenderType.guiTextHighlight(),
            highlightX - 1,
            y - 1,
            highlightX + highlightWidth,
            y + font.lineHeight + 1,
            0xFF0000FF
        );
    }
}
