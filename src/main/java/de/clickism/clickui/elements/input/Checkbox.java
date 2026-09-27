package de.clickism.clickui.elements.input;

import com.mojang.blaze3d.systems.RenderSystem;
import de.clickism.clickui.UiColor;
import de.clickism.clickui.UiElement;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.Border;
import de.clickism.clickui.util.versioning.VersionUtil;
//? if >= 26.1
//import net.minecraft.client.renderer.RenderPipelines;

import java.util.function.Consumer;

/**
 * A simple checkbox UI element that can be toggled on and off.
 */
public class Checkbox extends UiElement<Checkbox> {
    private static final int SIZE = 20;

    private boolean checked = false;

    private Consumer<Boolean> onCheckedChange = state -> {};

    /**
     * Creates a new Checkbox element.
     */
    public Checkbox() {
        this.onClick(event -> {
            event.playSound();
            this.toggle();
            this.onCheckedChange.accept(this.checked);
        });
        this.style(style()
            .borderPosition(Border.Position.INSIDE)
            .borderColor(UiColor.BLACK)
            .whenHovered(style()
                .borderColor(UiColor.WHITE)));
    }

    /**
     * Sets a listener that will be called whenever the checked state changes.
     *
     * @param listener the listener to call when the checked state changes
     * @return this Checkbox instance for method chaining
     */
    public Checkbox onCheckedChange(Consumer<Boolean> listener) {
        this.onCheckedChange = listener;
        return this;
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
        graphics.pose().pushPose();
        //? if < 26.1 {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        // Adjust scale to fit in the checkbox size
        graphics.pose().translate(bounds.x(), bounds.y(), 0.0F);
        graphics.pose().scale((float) bounds.width() / SIZE, (float) bounds.height() / SIZE, 1.0F);
        graphics.pose().translate(-bounds.x(), -bounds.y(), 0.0F);
        //?} elif >= 26.1 {
        /*graphics.pose().translate(bounds.x(), bounds.y());
        graphics.pose().scale((float) bounds.width() / SIZE, (float) bounds.height() / SIZE);
        graphics.pose().translate(-bounds.x(), -bounds.y());
        *///?}
        // Render the checkbox texture based on its state (focused and checked)

        var checkboxTexture = VersionUtil.checkboxTexture(checked);

        //? if < 1.21 {
        graphics.blit(
            checkboxTexture.texture(),
            bounds.x(),
            bounds.y(),
            0.0F,
            checkboxTexture.textureY(),
            SIZE,
            SIZE,
            64,
            64
        );
        //?} elif < 26.1 {
        /*graphics.blitSprite(checkboxTexture.texture(), bounds.x(), bounds.y(), SIZE, SIZE);
        *///?} elif >= 26.1 {
        /*graphics.blitSprite(RenderPipelines.GUI_TEXTURED, checkboxTexture.texture(), bounds.x(), bounds.y(), SIZE, SIZE);
        *///?}
        graphics.pose().popPose();
    }
}
