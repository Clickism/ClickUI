package de.clickism.clickui.elements.input;

import de.clickism.clickui.layout.Padding;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.BorderPosition;
import net.minecraft.client.renderer.RenderType;

import java.awt.*;

/**
 * A simple text field that allows users to input and edit text.
 */
public class TextField extends AbstractTextField<TextField> {
    private static final Padding DEFAULT_PADDING = Padding.create(5);
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int CURSOR_COLOR = Color.WHITE.getRGB();

    public TextField() {
        // Set default props
        // TODO: Better default height
        this.padding(DEFAULT_PADDING);
        // Set style
        this.style(s -> s
            .background(Color.BLACK)
            .border(new Color(0xFFA0A0A0))
            .borderPosition(BorderPosition.INSIDE)
            .ifHovered(h -> h
                .border(Color.WHITE))
            .ifFocused(f -> f
                .border(Color.WHITE))
        );
    }

    @Override
    public Size intrinsicSize() {
        var height = DEFAULT_HEIGHT - DEFAULT_PADDING.vertical();
        return new Size(DEFAULT_WIDTH, height);
    }

    @Override
    protected void renderText(RenderContext context, String text, int x, int y, boolean placeholder) {
        var graphics = context.graphics();
        // Enable scissor
        var bounds = bounds();
        graphics.enableScissor(
            bounds.x(),
            bounds.y(),
            bounds.x() + bounds.width(),
            bounds.y() + bounds.height()
        );
        // Render texts
        graphics.drawString(context.font(), text, x, y, Color.WHITE.getRGB());
        // Disable scissor
        graphics.disableScissor();
    }

    @Override
    protected void renderCursor(RenderContext context, int x, int y, boolean inline) {
        if (inline) {
            // Inline cursor as line
            var width = 1;
            var height = context.font().lineHeight;
            context.graphics().fill(RenderType.guiOverlay(), x, y, x + width, y + height, CURSOR_COLOR);
        } else {
            // Underscore cursor
            context.graphics().drawString(context.font(), "_", x, y, CURSOR_COLOR, false);
        }
    }

    @Override
    protected void renderHighlight(RenderContext context, int x, int y, int width) {
        // Render highlight rectangle
        context.graphics().fill(
            RenderType.guiTextHighlight(),
            x - 1,
            y - 1,
            x + width,
            y + context.font().lineHeight + 1,
            0xFF0000FF
        );
    }
}
