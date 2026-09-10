package de.clickism.clickui.elements.input;

import com.mojang.blaze3d.systems.RenderSystem;
import de.clickism.clickui.UiColor;
import de.clickism.clickui.UiElement;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.Border;
import net.minecraft.resources.ResourceLocation;

/**
 * A simple checkbox UI element that can be toggled on and off.
 */
public class Checkbox extends UiElement<Checkbox> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");
    private static final int SIZE = 20;

    private boolean checked = false;

    /**
     * Creates a new Checkbox element.
     */
    public Checkbox() {
        this.onClick(event -> {
            event.playSound();
            this.toggle();
        });
        this.style(style()
            .whenHovered(style()
                .borderPosition(Border.Position.INSIDE)
                .borderColor(UiColor.WHITE)));
    }

    @Override
    public Size intrinsicSize() {
        return new Size(SIZE, SIZE);
    }

    /**
     * Sets the checked state of the checkbox.
     *
     * @param checked the new checked state
     * @return this Checkbox instance for method chaining
     */
    public Checkbox checked(boolean checked) {
        this.checked = checked;
        return this;
    }

    /**
     * Toggles the checked state of the checkbox.
     *
     * @return this Checkbox instance for method chaining
     */
    public Checkbox toggle() {
        this.checked = !this.checked;
        return this;
    }

    /**
     * Returns the current checked state of the checkbox.
     *
     * @return true if the checkbox is checked, false otherwise
     */
    public boolean checked() {
        return this.checked;
    }

    @Override
    public void render(RenderContext context) {
        var graphics = context.graphics();
        var bounds = this.bounds();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        // Render the checkbox texture based on its state (focused and checked)
        graphics.blit(
            TEXTURE,
            bounds.x(),
            bounds.y(),
            0.0F,
            checked
                ? 20.0F
                : 0.0F,
            SIZE,
            SIZE,
            64,
            64
        );
    }
}
