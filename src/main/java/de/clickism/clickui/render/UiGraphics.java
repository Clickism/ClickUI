package de.clickism.clickui.render;

import com.mojang.math.Axis;
import de.clickism.clickui.UiElement;
import de.clickism.clickui.layout.Rect;
import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

//? if >= 26.1 {
import net.minecraft.client.renderer.RenderPipelines;
//? if >=26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
//?} else
//import com.mojang.blaze3d.pipeline.RenderPipeline;
//?} else {
//import net.minecraft.client.renderer.RenderType;
//?}

/**
 * A wrapper around gui graphics that provides version-independent rendering methods for UI elements.
 */
public class UiGraphics {
    private static final int WHITE = 0xFFFFFFFF;
    private final GuiGraphicsExtractor graphics;
    private float alpha = 1.0f;

    /**
     * Creates a new UiGraphics instance wrapping the given GuiGraphicsExtractor.
     *
     * @param graphics The GuiGraphicsExtractor instance to wrap.
     */
    public UiGraphics(GuiGraphicsExtractor graphics) {
        this.graphics = graphics;
    }

    /**
     * Returns the underlying GuiGraphicsExtractor instance.
     *
     * @return The GuiGraphicsExtractor instance.
     */
    public GuiGraphicsExtractor unwrap() {
        return graphics;
    }

    /**
     * Pushes the current transformation matrix onto the stack.
     */
    public void push() {
        //? if >= 26.1 {
        graphics.pose().pushMatrix();
        //?} else
        //graphics.pose().pushPose();
    }

    /**
     * Pops the current transformation matrix from the stack.
     */
    public void pop() {
        //? if >= 26.1 {
        graphics.pose().popMatrix();
        //?} else
        //graphics.pose().popPose();
    }

    /**
     * Translates the current transformation matrix by the given x and y offsets.
     *
     * @param x The x offset.
     * @param y The y offset.
     */
    public void translate(float x, float y) {
        graphics.pose().translate(
            x, y
            //? if < 26.1
            //, 0
        );
    }

    /**
     * Rotates the current transformation matrix by the given angle in radians.
     *
     * @param radians The angle in radians.
     */
    public void rotateRadians(float radians) {
        //? if >= 26.1 {
        graphics.pose().rotate(radians);
        //?} else {
        //graphics.pose().rotateAround(Axis.ZP.rotation(radians), 0, 0, 0);
        //?}
    }

    /**
     * Rotates the current transformation matrix by the given angle in degrees.
     *
     * @param degrees The angle in degrees.
     */
    public void rotateDegrees(float degrees) {
        rotateRadians((float) Math.toRadians(degrees));
    }

    /**
     * Rotates the current transformation matrix by the given angle in degrees about the specified point (x, y).
     *
     * @param x       The x coordinate of the point to rotate about.
     * @param y       The y coordinate of the point to rotate about.
     * @param degrees The angle in degrees.
     */
    public void rotateDegreesAbout(float x, float y, float degrees) {
        translate(x, y);
        rotateDegrees(degrees);
        translate(-x, -y);
    }

    /**
     * Scales the current transformation matrix by the given x and y factors.
     *
     * @param x The x scale factor.
     * @param y The y scale factor.
     */
    public void scale(float x, float y) {
        graphics.pose().scale(
            x, y
            //? if < 26.1
            //, 0
        );
    }

    /**
     * Scales the current transformation matrix by the given x and y factors about the specified point (x, y).
     *
     * @param x      The x coordinate of the point to scale about.
     * @param y      The y coordinate of the point to scale about.
     * @param scaleX The x scale factor.
     * @param scaleY The y scale factor.
     */
    public void scaleAbout(float x, float y, float scaleX, float scaleY) {
        translate(x, y);
        scale(scaleX, scaleY);
        translate(-x, -y);
    }

    /**
     * Executes the given action with scaling applied about the specified point (x, y).
     *
     * @param x       The x coordinate of the point to scale about.
     * @param y       The y coordinate of the point to scale about.
     * @param scaleX  The x scale factor.
     * @param scaleY  The y scale factor.
     * @param action  The action to execute with scaling applied.
     */
    public void withScaleAbout(float x, float y, float scaleX, float scaleY, Runnable action) {
        push();
        translate(x, y);
        scale(scaleX, scaleY);
        translate(-x, -y);
        action.run();
        pop();
    }


    /**
     * Enables scissor testing for the specified rectangular area.
     *
     * @param rect The rectangular area to enable scissor testing for.
     */
    public void enableScissor(Rect rect) {
        graphics.enableScissor(
            rect.x(),
            rect.y(),
            rect.x() + rect.width(),
            rect.y() + rect.height()
        );
    }

    /**
     * Enables scissor testing for the specified UI element.
     *
     * @param element The UI element to enable scissor testing for.
     */
    public void enableElementScissor(UiElement<?> element) {
        enableScissor(scissorBounds(element));
    }

    /**
     * Executes the given action with scissor testing enabled for the specified rectangular area.
     *
     * @param rect   The rectangular area to enable scissor testing for.
     * @param action The action to execute with scissor testing enabled.
     */
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

    /**
     * Executes the given action with scissor testing enabled for the specified UI element.
     *
     * @param element The UI element to enable scissor testing for.
     * @param action  The action to execute with scissor testing enabled.
     */
    public void withElementScissor(UiElement<?> element, Runnable action) {
        withScissor(scissorBounds(element), action);
    }

    /**
     * Returns the scissor bounds for the specified UI element.
     *
     * @param element The UI element to get the scissor bounds for.
     * @return The scissor bounds as a Rect object.
     */
    public Rect scissorBounds(UiElement<?> element) {
        //? if >= 26.1 {
        return element.bounds();
        //?} else
         //return element.renderBounds();
    }

    /**
     * Disables scissor testing.
     */
    public void disableScissor() {
        graphics.disableScissor();
    }

    /**
     * Fills a rectangular area with the specified color.
     *
     * @param x0    The x-coordinate of the top-left corner of the rectangle.
     * @param y0    The y-coordinate of the top-left corner of the rectangle.
     * @param x1    The x-coordinate of the bottom-right corner of the rectangle.
     * @param y1    The y-coordinate of the bottom-right corner of the rectangle.
     * @param color The color to fill the rectangle with (ARGB format).
     */
    public void fill(int x0, int y0, int x1, int y1, int color) {
        fill(RenderMode.GUI, x0, y0, x1, y1, withAlpha(color));
    }

    /**
     * Fills a rectangular area with the specified color using the given render mode.
     *
     * @param type  The render mode to use for filling the rectangle.
     * @param x0    The x-coordinate of the top-left corner of the rectangle.
     * @param y0    The y-coordinate of the top-left corner of the rectangle.
     * @param x1    The x-coordinate of the bottom-right corner of the rectangle.
     * @param y1    The y-coordinate of the bottom-right corner of the rectangle.
     * @param color The color to fill the rectangle with (ARGB format).
     */
    public void fill(RenderMode type, int x0, int y0, int x1, int y1, int color) {
        var pipeline = type.pipeline();
        graphics.fill(pipeline, x0, y0, x1, y1, withAlpha(color));
    }

    /**
     * Draws the outline of a rectangular area with the specified color.
     *
     * @param x      The x-coordinate of the top-left corner of the rectangle.
     * @param y      The y-coordinate of the top-left corner of the rectangle.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     * @param color  The color to draw the outline with (ARGB format).
     */
    public void outline(int x, int y, int width, int height, int color) {
        //~ if < 26.1 '.outline' -> '.renderOutline'
        graphics.outline(x, y, width, height, withAlpha(color));
    }

    /**
     * Draws the outline of a rectangular area with the specified color.
     *
     * @param rect  The rectangle to draw the outline for.
     * @param color The color to draw the outline with (ARGB format).
     */
    public void outline(Rect rect, int color) {
        outline(rect.x(), rect.y(), rect.width(), rect.height(), withAlpha(color));
    }

    /**
     * Draws a horizontal line between two points with the specified color.
     *
     * @param x0    The x-coordinate of the starting point of the line.
     * @param x1    The x-coordinate of the ending point of the line.
     * @param y     The y-coordinate of the line.
     * @param color The color to draw the line with (ARGB format).
     */
    public void horizontalLine(int x0, int x1, int y, int color) {
        fill(x0, y, x1 + 1, y + 1, withAlpha(color));
    }

    /**
     * Draws a vertical line between two points with the specified color.
     *
     * @param x      The x-coordinate of the line.
     * @param y0     The y-coordinate of the starting point of the line.
     * @param y1     The y-coordinate of the ending point of the line.
     * @param color  The color to draw the line with (ARGB format).
     */
    public void verticalLine(int x, int y0, int y1, int color) {
        fill(x, y0 + 1, x + 1, y1, withAlpha(color));
    }

    //~ if < 26.1 '.text' -> '.drawString' {

    /**
     * Draws text at the specified position with the given color and drop shadow option.
     *
     * @param string      The text to draw.
     * @param x           The x-coordinate of the text's starting position.
     * @param y           The y-coordinate of the text's starting position.
     * @param color       The color to draw the text with (ARGB format).
     * @param dropShadow  Whether to draw a drop shadow behind the text.
     */
    public void text(String string, int x, int y, int color, boolean dropShadow) {
        graphics.text(Util.font(), string, x, y, withAlpha(color), dropShadow);
    }

    /**
     * Draws formatted text at the specified position with the given color and drop shadow option.
     *
     * @param text        The formatted text to draw.
     * @param x           The x-coordinate of the text's starting position.
     * @param y           The y-coordinate of the text's starting position.
     * @param color       The color to draw the text with (ARGB format).
     * @param dropShadow  Whether to draw a drop shadow behind the text.
     */
    public void text(FormattedCharSequence text, int x, int y, int color, boolean dropShadow) {
        graphics.text(Util.font(), text, x, y, withAlpha(color), dropShadow);
    }

    /**
     * Draws a component at the specified position with the given color and drop shadow option.
     *
     * @param text        The component to draw.
     * @param x           The x-coordinate of the text's starting position.
     * @param y           The y-coordinate of the text's starting position.
     * @param color       The color to draw the text with (ARGB format).
     * @param dropShadow  Whether to draw a drop shadow behind the text.
     */
    public void text(Component text, int x, int y, int color, boolean dropShadow) {
        graphics.text(Util.font(), text, x, y, withAlpha(color), dropShadow);
    }

    //~}

    /**
     * Renders the background for a tooltip within the specified bounds using the given style.
     *
     * @param bounds The rectangular bounds of the tooltip background.
     * @param style  The style identifier for the tooltip background.
     */
    public void tooltipBackground(Rect bounds, @Nullable Identifier style) {
        //~ if < 26.1 '.extractTooltipBackground' -> '.renderTooltipBackground'
        TooltipRenderUtil.extractTooltipBackground(
            graphics,
            bounds.x(),
            bounds.y(),
            bounds.width(),
            bounds.height()
            //? if >= 26.1 {
            , style
            //?} else
            //, 0
        );
    }

    /**
     * Sets the alpha value for rendering.
     *
     * @param alpha The alpha value to set (0.0f to 1.0f).
     */
    public void alpha(float alpha) {
        this.alpha = alpha;
        // Set color globally
        //? if < 26.1
        //graphics.setColor(1.0f, 1.0f, 1.0f, alpha);
    }

    /**
     * Returns the current alpha value for rendering.
     *
     * @return The current alpha value (0.0f to 1.0f).
     */
    public float alpha() {
        return this.alpha;
    }

    /**
     * Applies the current alpha value to the given color and returns the resulting color.
     * <p>
     * If global alpha is already applied (in versions < 26.1), this method will return the original color.
     *
     * @param color The original color (ARGB format).
     * @return The color with the current alpha value applied (ARGB format).
     */
    public int withAlpha(int color) {
        //? if <26.1 {
        /*// Alpha is applied globally already here
        return color;
        *///?} else {
        int a = (int) ((color >> 24 & 0xFF) * alpha);
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
        //?}
    }

    /**
     * Renders a sprite at the specified position and size.
     *
     * @param sprite The sprite to render.
     * @param x      The x-coordinate of the top-left corner of the sprite.
     * @param y      The y-coordinate of the top-left corner of the sprite.
     * @param width  The width of the sprite.
     * @param height The height of the sprite.
     */
    public void renderSprite(Sprite sprite, int x, int y, int width, int height) {
        //? if >= 1.21.1 {
        graphics.blitSprite(
            //? if >= 26.1
            RenderPipelines.GUI_TEXTURED,
            sprite.texture(),
            x, y,
            width, height
            //? if >= 26.1
            ,alpha
        );
        //?} else {
        /*graphics.blit(
            sprite.texture(),
            x, y,
            sprite.textureX(), sprite.textureY(),
            width, height,
            sprite.uWidth(), sprite.vHeight()
        );
        *///?}
    }

    /**
     * Renders a sliced sprite at the specified position and size.
     *
     * @param sprite The sprite to render.
     * @param x      The x-coordinate of the top-left corner of the sprite.
     * @param y      The y-coordinate of the top-left corner of the sprite.
     * @param width  The width of the sprite.
     * @param height The height of the sprite.
     */
    public void renderSpriteSliced(Sprite sprite, int x, int y, int width, int height) {
        //? if >= 1.21.1 {
        graphics.blitSprite(
            //? if >= 26.1
            RenderPipelines.GUI_TEXTURED,
            sprite.texture(),
            x, y,
            width, height
            //? if >= 26.1
            ,alpha
        );
        //?} else {
        /*graphics.blitNineSliced(
            sprite.texture(),
            x, y,
            width, height,
            sprite.sliceWidth(), sprite.sliceHeight(),
            sprite.uWidth(), sprite.vHeight(),
            sprite.textureX(), sprite.textureY()
        );
        *///?}
    }

    /**
     * Renders an image at the specified position and size.
     *
     * @param texture The texture identifier of the image to render.
     * @param x       The x-coordinate of the top-left corner of the image.
     * @param y       The y-coordinate of the top-left corner of the image.
     * @param width   The width of the image.
     * @param height  The height of the image.
     */
    public void renderImage(Identifier texture, int x, int y, int width, int height) {
        graphics.blit(
            //? if >= 26.1
            RenderPipelines.GUI_TEXTURED,
            texture,
            x, y,
            0, 0,
            width, height,
            width, height
            //? if >= 26.1
            ,withAlpha(WHITE)
        );
    }

    /**
     * Represents the rendering mode for UI elements.
     */
    public enum RenderMode {
        GUI,
        GUI_TEXT_HIGHLIGHT;

        //? if >= 26.1 {
        public RenderPipeline pipeline() {
            return switch (this) {
                case GUI -> RenderPipelines.GUI;
                case GUI_TEXT_HIGHLIGHT -> RenderPipelines.GUI_TEXT_HIGHLIGHT;
            };
        }
        //?} else {
        /*public RenderType pipeline() {
            return switch (this) {
                case GUI -> RenderType.gui();
                case GUI_TEXT_HIGHLIGHT -> RenderType.guiTextHighlight();
            };
        }
        *///?}
    }

    /**
     * Represents a sprite with its texture and slicing information.
     *
     * @param texture     The texture identifier of the sprite.
     * @param sliceWidth  The width of the slice for nine-slice rendering.
     * @param sliceHeight The height of the slice for nine-slice rendering.
     * @param uWidth      The width of the texture in pixels.
     * @param vHeight     The height of the texture in pixels.
     * @param textureX    The x-coordinate of the texture in the atlas.
     * @param textureY    The y-coordinate of the texture in the atlas.
     */
    public record Sprite(
        Identifier texture,
        int sliceWidth,
        int sliceHeight,
        int uWidth,
        int vHeight,
        int textureX,
        int textureY
    ) {
        /**
         * Creates a new Sprite instance with the specified texture and default slicing and texture coordinates.
         *
         * @param texture The texture identifier of the sprite.
         */
        public Sprite(Identifier texture) {
            this(texture, 0, 0, 0, 0, 0, 0);
        }
    }
}
