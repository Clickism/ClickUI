package de.clickism.clickui.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import de.clickism.clickui.UiElement;
import de.clickism.clickui.layout.Rect;
import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

public class UiGraphics {
    private final GuiGraphics graphics;

    public UiGraphics(GuiGraphics graphics) {
        this.graphics = graphics;
    }

    /**
     * Returns the underlying GuiGraphics instance.
     *
     * @return The GuiGraphics instance.
     */
    public GuiGraphics unwrap() {
        return graphics;
    }

    public void push() {
        graphics.pose().pushPose();
    }

    public void pop() {
        graphics.pose().popPose();
    }

    public void translate(float x, float y) {
        graphics.pose().translate(x, y);
    }

    public void scale(float x, float y) {
        graphics.pose().scale(x, y);
    }

    public void scaleAbout(float x, float y, float scaleX, float scaleY) {
        graphics.pose().translate(x, y);
        graphics.pose().scale(scaleX, scaleY);
        graphics.pose().translate(-x, -y);
    }

    public void enableScissor(Rect rect) {
        graphics.enableScissor(
            rect.x(),
            rect.y(),
            rect.x() + rect.width(),
            rect.y() + rect.height()
        );
    }

    public void enableElementScissor(UiElement<?> element) {
        enableScissor(scissorBounds(element));
    }

    public void withScissor(Rect rect, Runnable action) {
        push();
        graphics.enableScissor(
            rect.x(),
            rect.y(),
            rect.x() + rect.width(),
            rect.y() + rect.height()
        );
        action.run();
        graphics.disableScissor();
        pop();
    }

    public void withElementScissor(UiElement<?> element, Runnable action) {
        withScissor(scissorBounds(element), action);
    }

    public Rect scissorBounds(UiElement<?> element) {
        //? if >= 26.1 {
        /*return element.bounds();
        *///?} else
         return element.renderBounds();
    }

    public void disableScissor() {
        graphics.disableScissor();
    }

    public void withScaleAround(float x, float y, float scaleX, float scaleY, Runnable action) {
        push();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scaleX, scaleY);
        graphics.pose().translate(-x, -y);
        action.run();
        pop();
    }

    public void fill(int x0, int y0, int x1, int y1, int color) {
        graphics.fill(RenderPipelines.GUI, x0, y0, x1, y1, color);
    }

    public void fill(RenderType type, int x0, int y0, int x1, int y1, int color) {
        var pipeline = type.pipeline();
        graphics.fill(pipeline, x0, y0, x1, y1, color);
    }

    public void outline(int x, int y, int width, int height, int color) {
        graphics.outline(x, y, width, height, color);
    }

    public void outline(Rect rect, int color) {
        outline(rect.x(), rect.y(), rect.width(), rect.height(), color);
    }

    public void text(String string, int x, int y, int color, boolean dropShadow) {
        graphics.text(Util.font(), string, x, y, color, dropShadow);
    }

    public void text(FormattedCharSequence text, int x, int y, int color, boolean dropShadow) {
        graphics.text(Util.font(), text, x, y, color, dropShadow);
    }

    public void text(Component text, int x, int y, int color, boolean dropShadow) {
        graphics.text(Util.font(), text, x, y, color, dropShadow);
    }

    public void tooltipBackground(Rect bounds, ResourceLocation style) {
        TooltipRenderUtil.extractTooltipBackground(
            graphics,
            bounds.x(),
            bounds.y(),
            bounds.width(),
            bounds.height(),
            style
            //TODO: , -1
        );
    }

    public enum RenderType {
        GUI,
        GUI_TEXT_HIGHLIGHT;

        public RenderPipeline pipeline() {
            return switch (this) {
                case GUI -> RenderPipelines.GUI;
                case GUI_TEXT_HIGHLIGHT -> RenderPipelines.GUI_TEXT_HIGHLIGHT;
            };
        }
    }
}
