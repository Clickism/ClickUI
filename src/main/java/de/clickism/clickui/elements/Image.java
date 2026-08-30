package de.clickism.clickui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import net.minecraft.resources.ResourceLocation;

/**
 * A UI element that displays an image from a specified texture resource.
 */
public class Image extends Element<Image> {
    private ResourceLocation texture;
    private final int width;
    private final int height;

    /**
     * Creates a new Image element with the specified texture.
     *
     * @param texture the resource location of the texture to display
     */
    public Image(ResourceLocation texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the texture of the image.
     *
     * @param texture the new texture resource location
     * @return this Image element for method chaining
     */
    public Image texture(ResourceLocation texture) {
        this.texture = texture;
        invalidateLayout();
        return this;
    }

    @Override
    public Size intrinsicSize() {
        if (texture == null) {
            return Size.ZERO;
        }
        return new Size(width, height);
    }

    @Override
    public Size defaultMinSize() {
        // Don't shrink by default
        return intrinsicSize();
    }

    @Override
    public void render(RenderContext context) {
        if (texture == null) return;
        var graphics = context.graphics();
        var bounds = bounds();
        // Override render to enable blending for semi-transparent textures
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Render image
        graphics.blit(
            texture,
            bounds.x(),
            bounds.y(),
            0, 0,
            bounds.width(), bounds.height(),
            bounds.width(), bounds.height()
        );
        // Revert blending
        RenderSystem.disableBlend();
    }
}
