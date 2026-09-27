package de.clickism.clickui.render;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.layout.Rect;
import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

//? if >= 1.21.1 {
/*import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?} else {
import net.minecraft.client.renderer.RenderType;
//?}

public class UiGraphics {
    private static final int WHITE = 0xFFFFFFFF;
    private final GuiGraphics graphics;
    private float alpha = 1.0f;

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
        graphics.pose().translate(
            x, y
            //? if < 26.1
            , 0
        );
    }

    public void scale(float x, float y) {
        graphics.pose().scale(
            x, y
            //? if < 26.1
            , 0
        );
    }

    public void scaleAbout(float x, float y, float scaleX, float scaleY) {
        translate(x, y);
        scale(scaleX, scaleY);
        translate(-x, -y);
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

    public void withScaleAbout(float x, float y, float scaleX, float scaleY, Runnable action) {
        push();
        translate(x, y);
        scale(scaleX, scaleY);
        translate(-x, -y);
        action.run();
        pop();
    }

    public void fill(int x0, int y0, int x1, int y1, int color) {
        fill(RenderMode.GUI, x0, y0, x1, y1, withAlpha(color));
    }

    public void fill(RenderMode type, int x0, int y0, int x1, int y1, int color) {
        var pipeline = type.pipeline();
        graphics.fill(pipeline, x0, y0, x1, y1, withAlpha(color));
    }

    public void outline(int x, int y, int width, int height, int color) {
        //~ if < 26.1 '.outline' -> '.renderOutline'
        graphics.renderOutline(x, y, width, height, withAlpha(color));
    }

    public void outline(Rect rect, int color) {
        outline(rect.x(), rect.y(), rect.width(), rect.height(), withAlpha(color));
    }

    //~ if < 26.1 '.text' -> '.drawString' {
    public void text(String string, int x, int y, int color, boolean dropShadow) {
        graphics.drawString(Util.font(), string, x, y, withAlpha(color), dropShadow);
    }

    public void text(FormattedCharSequence text, int x, int y, int color, boolean dropShadow) {
        graphics.drawString(Util.font(), text, x, y, withAlpha(color), dropShadow);
    }

    public void text(Component text, int x, int y, int color, boolean dropShadow) {
        graphics.drawString(Util.font(), text, x, y, withAlpha(color), dropShadow);
    }
    //~}

    public void tooltipBackground(Rect bounds, ResourceLocation style) {
        TooltipRenderUtil
            //? if >= 26.1 {
            /*.extractTooltipBackground
            *///?} else
            .renderTooltipBackground
        (
            graphics,
            bounds.x(),
            bounds.y(),
            bounds.width(),
            bounds.height()
            //? if >= 26.1 {
            /*, style
            *///?} else
            , 0
        );
    }

    public void alpha(float alpha) {
        this.alpha = alpha;
    }

    public float alpha() {
        return this.alpha;
    }

    public int withAlpha(int color) {
        int a = (int) ((color >> 24 & 0xFF) * alpha);
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    public void renderSprite(Sprite sprite, int x, int y, int width, int height) {
        //? if >= 1.21.1 {
        /*graphics.blitSprite(
            //? if >= 26.1
            //RenderPipelines.GUI_TEXTURED,
            sprite.texture(),
            x, y,
            width, height,
            alpha
        );
        *///?} else {
        graphics.blit(
            sprite.texture(),
            x, y,
            sprite.textureX(), sprite.textureY(),
            width, height,
            sprite.uWidth(), sprite.vHeight()
        );
        //?}
    }

    public void renderSpriteSliced(Sprite sprite, int x, int y, int width, int height) {
        //? if >= 1.21.1 {
        /*graphics.blitSprite(
            //? if >= 26.1
            //RenderPipelines.GUI_TEXTURED,
            sprite.texture(),
            x, y,
            width, height,
            alpha
        );
        *///?} else {
        graphics.blitNineSliced(
            sprite.texture(),
            x, y,
            width, height,
            sprite.sliceWidth(), sprite.sliceHeight(),
            sprite.uWidth(), sprite.vHeight(),
            sprite.textureX(), sprite.textureY()
        );
        //?}
    }

    public void renderImage(ResourceLocation texture, int x, int y, int width, int height) {
        graphics.blit(
            //? if >= 26.1
            //RenderPipelines.GUI,
            texture,
            x, y,
            0, 0,
            width, height,
            width, height,
            withAlpha(WHITE)
        );
    }

    public enum RenderMode {
        GUI,
        GUI_TEXT_HIGHLIGHT;

        //? if >= 26.1 {
        /*public RenderPipeline pipeline() {
            return switch (this) {
                case GUI -> RenderPipelines.GUI;
                case GUI_TEXT_HIGHLIGHT -> RenderPipelines.GUI_TEXT_HIGHLIGHT;
            };
        }
        *///?} else {
        public RenderType pipeline() {
            return switch (this) {
                case GUI -> RenderType.gui();
                case GUI_TEXT_HIGHLIGHT -> RenderType.guiTextHighlight();
            };
        }
        //?}
    }

    public record Sprite(
        ResourceLocation texture,
        int sliceWidth,
        int sliceHeight,
        int uWidth,
        int vHeight,
        int textureX,
        int textureY
    ) {
        public Sprite(ResourceLocation texture) {
            this(texture, 0, 0, 0, 0, 0, 0);
        }
    }
}
