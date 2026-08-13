package de.clickism.clickui;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Represents the render context when rendering a UI element.
 *
 * @param graphics
 * @param mouseX
 * @param mouseY
 * @param delta
 */
public record RenderContext(
        GuiGraphics graphics,
        int mouseX,
        int mouseY,
        float delta
) {
}
