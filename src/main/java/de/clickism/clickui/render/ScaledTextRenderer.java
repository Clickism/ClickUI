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
        renderWithScale(x, y, scale, () -> {
            context.graphics().drawString(Util.font(), text, 0, 0, color);
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
        renderWithScale(x, y, scale, () -> {
            context.graphics().drawString(Util.font(), text, 0, 0, color);
        });
    }

    private void renderWithScale(int x, int y, float scale, Runnable render) {
        var graphics = context.graphics();
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1);

        render.run();

        graphics.pose().popPose();
    }

    public float measureWidth(Component text, float scale) {
        return Util.font().width(text) * scale;
    }

    public float measureHeight(float scale) {
        return Util.font().lineHeight * scale;
    }
}
