package de.clickism.clickui.render;

import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Represents the render context when rendering a UI element.
 *
 * @param graphics
 * @param mouseX
 * @param mouseY
 * @param delta
 * @param debug
 */
public record RenderContext(
    GuiGraphics graphics,
    int mouseX,
    int mouseY,
    int screenWidth,
    int screenHeight,
    float delta,
    boolean debug
) {
    /**
     * Returns the font renderer used for rendering text.
     *
     * @return The font renderer.
     */
    public Font font() {
        return Util.font();
    }

    /**
     * Returns a new RenderContext with the specified debug flag.
     *
     * @param debug Whether to enable debug mode.
     * @return A new RenderContext with the specified debug flag.
     */
    public RenderContext withDebug(boolean debug) {
        return new RenderContext(graphics, mouseX, mouseY, screenWidth, screenHeight, delta, debug);
    }
}
