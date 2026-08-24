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
}
