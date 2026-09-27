package de.clickism.clickui.render;

import de.clickism.clickui.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

/**
 * A renderer for scaled text that uses a RenderContext to draw text at a specified position with a specified scale and color.
 *
 * @param context the RenderContext to use for rendering
 */
public record ScaledTextRenderer(
    RenderContext context
) {
    /**
     * Renders the given text at the specified position with the specified scale and color.
     *
     * @param text  the text to render
     * @param x     the x position to render the text at
     * @param y     the y position to render the text at
     * @param scale the scale to render the text at
     * @param color the color to render the text with
     */
    public void render(Component text, int x, int y, float scale, int color) {
        render(text, x, y, scale, color, true);
    }

    /**
     * Renders the given text at the specified position with the specified scale, color, and shadow option.
     *
     * @param text   the text to render
     * @param x      the x position to render the text at
     * @param y      the y position to render the text at
     * @param scale  the scale to render the text at
     * @param color  the color to render the text with
     * @param shadow whether to render a shadow
     */
    public void render(Component text, int x, int y, float scale, int color, boolean shadow) {
        context.graphics().withScaleAbout(x, y, scale, scale, () -> {
            context.graphics().text(text, x, y, color, shadow);
        });
    }

    /**
     * Renders the given text at the specified position with the specified scale and color.
     *
     * @param text  the text to render
     * @param x     the x position to render the text at
     * @param y     the y position to render the text at
     * @param scale the scale to render the text at
     * @param color the color to render the text with
     */
    public void render(FormattedCharSequence text, int x, int y, float scale, int color) {
        render(text, x, y, scale, color, true);
    }

    /**
     * Renders the given text at the specified position with the specified scale, color, and shadow option.
     *
     * @param text   the text to render
     * @param x      the x position to render the text at
     * @param y      the y position to render the text at
     * @param scale  the scale to render the text at
     * @param color  the color to render the text with
     * @param shadow whether to render a shadow
     */
    public void render(FormattedCharSequence text, int x, int y, float scale, int color, boolean shadow) {
        context.graphics().withScaleAbout(x, y, scale, scale, () -> {
            context.graphics().text(text, x, y, color, shadow);
        });
    }

    public float measureWidth(Component text, float scale) {
        return Util.font().width(text) * scale;
    }

    public float measureWidth(FormattedCharSequence text, float scale) {
        return Util.font().width(text) * scale;
    }

    public float measureHeight(float scale) {
        return Util.font().lineHeight * scale;
    }
}
