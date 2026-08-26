package de.clickism.clickui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Padding;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.render.ScaledTextRenderer;
import de.clickism.clickui.style.BorderPosition;
import de.clickism.clickui.util.Util;
import net.minecraft.network.chat.Component;

import java.awt.*;

import static net.minecraft.client.gui.components.AbstractWidget.WIDGETS_LOCATION;

/**
 * A simple UI element that can be clicked and displays a label.
 */
public class Button extends Element<Button> {
    private static final int DEFAULT_HEIGHT = 20;
    private static final Padding DEFAULT_PADDING = Padding.create(4, 8);

    /**
     * The label to display on the button.
     */
    private Component label;

    /**
     * Creates a new Button element with the specified label.
     *
     * @param label the label to display on the button
     */
    public Button(Component label) {
        this.label = label;
        // Adjust default padding
        // TODO: Fix default pading? why these values
        this.padding(DEFAULT_PADDING);
        this.style(s -> s
            .whenHovered(h -> h
                .border(Color.WHITE)
                .borderPosition(BorderPosition.CENTER)));
        // Play down sound on click
        this.onClick(event -> Util.playDownSound());
    }

    /**
     * Creates a new Button element with the specified label as a String.
     *
     * @param label the label to display on the button
     */
    public Button(String label) {
        this(Component.literal(label));
    }

    /**
     * Sets the label of the button.
     *
     * @param label the label to set
     */
    public void label(Component label) {
        this.label = label;
        this.invalidate();
    }

    @Override
    public Size intrinsicSize() {
        // TODO: Consider font size
        var height = DEFAULT_HEIGHT - DEFAULT_PADDING.vertical();
        var width = Util.font().width(label);
        return new Size(width, height);
    }

    @Override
    public void render(RenderContext context) {
        var graphics = context.graphics();
        var bounds = this.bounds();
        if (bounds.isEmpty()) {
            return; // Avoid division by zero or rendering issues if bounds are empty
        }
        // Override render to enable blending for semi-transparent textures
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Render button texture
        graphics.blitNineSliced(
            WIDGETS_LOCATION,
            bounds.x(), bounds.y(), bounds.width(), bounds.height(),
            20, 4, 200, 20, 0, textureY()
        );
        // Revert blending
        RenderSystem.disableBlend();
        // Render label
        // TODO: Move outline into real border logic
        // TODO: Scrolling text if it doesn't fit in the button
        var fontScale = resolvedStyle().fontScale();
        var renderer = new ScaledTextRenderer(context);
        // Center the label vertically and horizontally
        var textWidth = renderer.measureWidth(label, fontScale);
        var textHeight = renderer.measureHeight(fontScale);
        var textX = (int) (bounds.x() + (bounds.width() - textWidth) / 2);
        var textY = (int) (bounds.y() + (bounds.height() - textHeight) / 2);
        textY += 1; // Adjust for better visual alignment
        // Text color
        var color = state().disabled()
                    ? 0xFFAAAAAA
                    : 0xFFFFFFFF;
        renderer.render(label, textX, textY, fontScale, color);
    }

    /**
     * Calculates the Y position of the button texture based on the button's state.
     *
     * @return the Y position of the button texture
     */
    private int textureY() {
        int textureY = 46;
        if (!state().disabled()) {
            textureY += 20;
        }
        return textureY;
    }
}
