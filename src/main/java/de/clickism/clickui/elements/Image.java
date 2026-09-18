package de.clickism.clickui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import de.clickism.clickui.UiElement;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import net.minecraft.resources.ResourceLocation;

/**
 * A UI element that displays an image from a specified texture resource.
 */
public class Image extends UiElement<Image> {
    private final int width;
    private final int height;
    private ResourceLocation texture;
    private boolean keepAspectRatio = false;

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
     * Fits the given width and height into the specified maximum dimensions while maintaining the specified aspect ratio.
     *
     * @param width       the original width
     * @param height      the original height
     * @param maxWidth    the maximum allowed width
     * @param maxHeight   the maximum allowed height
     * @param aspectRatio the desired aspect ratio (width / height)
     * @return a Size object representing the fitted dimensions
     */
    private static Size fitAspectRatio(
        int width,
        int height,
        int maxWidth,
        int maxHeight,
        double aspectRatio
    ) {
        if (aspectRatio <= 0) {
            return new Size(
                Math.min(width, maxWidth),
                Math.min(height, maxHeight)
            );
        }

        // Start from the requested width.
        width = Math.min(width, maxWidth);
        height = (int) Math.ceil(width / aspectRatio);

        // Height overflow -> shrink both dimensions.
        if (height > maxHeight) {
            height = maxHeight;
            width = (int) Math.floor(height * aspectRatio);
        }

        // Width overflow -> shrink both dimensions.
        if (width > maxWidth) {
            width = maxWidth;
            height = (int) Math.floor(width / aspectRatio);
        }

        return new Size(width, height);
    }

    /**
     * Sets whether to keep the aspect ratio of the image when growing.
     *
     * @param keepAspectRatio true to keep the aspect ratio, false otherwise
     * @return this Image element for method chaining
     */
    public Image keepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
        return this;
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
        if (keepAspectRatio) {
            return fitAspectRatio(
                width,
                height,
                width().max(),
                height().max(),
                (double) width / height
            );
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
