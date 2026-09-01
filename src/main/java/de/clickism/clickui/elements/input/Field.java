package de.clickism.clickui.elements.input;

import de.clickism.clickui.UiColor;
import de.clickism.clickui.layout.Padding;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.Border;
import de.clickism.clickui.util.Util;
import net.minecraft.client.renderer.RenderType;

/**
 * A simple implementation of a text field with default styling and behavior.
 */
public abstract class Field<S extends Field<S>> extends AbstractField<S> {
    private static final Padding DEFAULT_PADDING = Padding.create(6, 5, 5, 5);
    private static final int DEFAULT_WIDTH = 100;

    // TODO: Move into custom style props
    private static final UiColor TEXT_COLOR = UiColor.rgb(0xE0E0E0);
    private static final UiColor INVALID_COLOR = UiColor.rgb(0xFF5555);

    public Field() {
        // Set default props
        // TODO: Better default height
        this.padding(DEFAULT_PADDING);
        // Set style
        this.style(style()
            .backgroundColor(UiColor.BLACK)
            .borderColor(UiColor.rgb(0xA0A0A0))
            .borderPosition(Border.Position.INSIDE)
            .whenHovered(style()
                .borderColor(UiColor.WHITE))
            .whenFocused(style()
                .borderColor(UiColor.WHITE))
        );
    }

    /**
     * Determines the text color based on the state of the text field.
     *
     * @param placeholder Indicates whether the text being rendered is a placeholder.
     * @return The RGB color value for the text.
     */
    protected int textColor(boolean placeholder) {
        if (invalid()) {
            return INVALID_COLOR.color();
        }
        if (placeholder) {
            return UiColor.GRAY.color();
        }
        return TEXT_COLOR.color();
    }

    @Override
    public Size intrinsicSize() {
        var height = Util.font().lineHeight;
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
        graphics.drawString(context.font(), sugestion, x, y, UiColor.GRAY.color());
    }

    @Override
    protected void renderCursor(RenderContext context, int x, int y, boolean inline) {
        var color = textColor(false);
        if (inline) {
            // Inline cursor as line
            y -= 1; // Render slightly above the text for better visibility
            var width = 1;
            var height = context.font().lineHeight + 1;
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
