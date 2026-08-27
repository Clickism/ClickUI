package de.clickism.clickui.render;

import com.mojang.blaze3d.systems.RenderSystem;
import de.clickism.clickui.Element;
import de.clickism.clickui.UiScreen;
import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;

/**
 * Renders a tooltip element at the mouse position, ensuring it is displayed above other UI elements.
 */
public class TooltipRenderer {
    private static final int TOOLTIP_Z_INDEX = 1000; // Render tooltips above other elements

    private final Element<?> tooltip;
    private final RenderContext context;

    /**
     * Creates a new TooltipRenderer for the specified tooltip element and render context.
     * The tooltip should either be already layed out, or be marked as dirty/invalidated.
     *
     * @param tooltip The tooltip element to render.
     * @param context The render context.
     */
    public TooltipRenderer(Element<?> tooltip, RenderContext context) {
        this.tooltip = tooltip;
        this.context = context;
    }

    /**
     * Prepares the tooltip for rendering by initializing and laying it out if needed.
     */
    private void prepare() {
        if (!tooltip.isDirty()) return;
        // Initialize element
        Util.preOrder(this.tooltip, Element::initialize);
        // Layout element
        new LayoutEngine().layout(tooltip);
        // Clear dirty
        tooltip.clearDirty();
    }

    /**
     * Renders the tooltip at the current mouse position.
     */
    public void render() {
        var screen = UiScreen.current();
        if (screen == null) return;

        // Prepare render
        prepare();
        // Render at the mouse position
        var offset = TooltipRenderUtil.MOUSE_OFFSET;
        // Calculate the position of the tooltip
        double tooltipX = context.mouseX() + offset;
        double tooltipY = context.mouseY() + offset;

        // Render the tooltip at the calculated position
        var graphics = context.graphics();
        graphics.pose().pushPose();
        graphics.pose().translate(tooltipX, tooltipY, TOOLTIP_Z_INDEX);

        // Render background with padding
        var bounds = tooltip.bounds();
        TooltipRenderUtil.renderTooltipBackground(
            context.graphics(),
            bounds.x(),
            bounds.y(),
            bounds.width(),
            bounds.height(),
            -1 // Render behind actual tooltip
        );
        // Render tooltip content
        tooltip.renderTree(context);
        graphics.pose().popPose();
    }
}
