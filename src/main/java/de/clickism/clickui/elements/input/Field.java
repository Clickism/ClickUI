package de.clickism.clickui.elements.input;

import de.clickism.clickui.layout.Padding;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.BorderPosition;
import net.minecraft.client.renderer.RenderType;

import java.awt.*;

/**
 * A simple implementation of a text field with default styling and behavior.
 */
public abstract class Field<S extends Field<S>> extends AbstractField<S> {
    private static final Padding DEFAULT_PADDING = Padding.create(5);
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 20;

    // TODO: Move into custom style props
    private static final Color TEXT_COLOR = new Color(0xFFE0E0E0, true);
    private static final Color INVALID_COLOR = new Color(0xFFFF5555, true);

    public Field() {
        // Set default props
        // TODO: Better default height
        this.padding(DEFAULT_PADDING);
        // Set style
        this.style(s -> s
            .background(Color.BLACK)
            .border(new Color(0xFFA0A0A0))
            .borderPosition(BorderPosition.INSIDE)
            .whenHovered(h -> h
                .border(Color.WHITE))
            .whenFocused(f -> f
                .border(Color.WHITE))
        );
    }

    /**
     * Determines the text color based on the state of the text field.
     *
     * @param placeholder Indicates whether the text being rendered is a placeholder.
     * @return The RGB color value for the text.
     */
    private int textColor(boolean placeholder) {
        if (invalid()) {
            return INVALID_COLOR.getRGB();
        }
        if (placeholder) {
            return Color.GRAY.getRGB();
        }
        return TEXT_COLOR.getRGB();
    }

    @Override
    public Size intrinsicSize() {
        var height = DEFAULT_HEIGHT - DEFAULT_PADDING.vertical();
        return new Size(DEFAULT_WIDTH, height);
    }

    @Override
    protected void renderText(
        RenderContext context,
        String text,
        int x,
        int y,
        boolean placeholder,
        String sugestion
    ) {
        var graphics = context.graphics();
        // Render text
        var color = textColor(placeholder);
        graphics.drawString(context.font(), text, x, y, color);
        // Render suggestion
        x += context.font().width(text);
        graphics.drawString(context.font(), sugestion, x, y, Color.GRAY.getRGB());
    }

    @Override
    protected void renderCursor(RenderContext context, int x, int y, boolean inline) {
        var color = textColor(false);
        if (inline) {
            // Inline cursor as line
            var width = 1;
            var height = context.font().lineHeight;
            context.graphics().fill(RenderType.guiOverlay(), x, y, x + width, y + height, color);
        } else {
            // Underscore cursor
            context.graphics().drawString(context.font(), "_", x, y, color, false);
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
