package de.clickism.clickui;

import de.clickism.clickui.event.HitTester;
import de.clickism.clickui.event.events.MouseClickEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * A screen that handles hovered elements and all events,
 * and propagates them to the element tree.
 */
public abstract class UiEventScreen extends Screen {
    /**
     * Keep track of the hovered element
     */
    private @Nullable Element<?> hoveredElement = null;

    private final HitTester hitTester = new HitTester();

    protected UiEventScreen(Component component) {
        super(component);
    }

    /**
     * Returns the root element of the UI tree.
     *
     * @return the root element
     */
    protected abstract Element<?> eventRoot();

    /**
     * Updates the hovered element and its state.
     *
     * @param element the element that is currently hovered, or null if no element is hovered
     */
    protected void hoveredElement(@Nullable Element<?> element) {
        // Update hovered state
        if (hoveredElement != null && hoveredElement != element) {
            hoveredElement.state().hovered(false);
        }
        hoveredElement = element;
        if (hoveredElement != null) {
            hoveredElement.state().hovered(true);
        }
    }

    /**
     * Returns the currently hovered element, or null if no element is hovered.
     *
     * @return the currently hovered element, or null if no element is hovered
     */
    protected @Nullable Element<?> hoveredElement() {
        return hoveredElement;
    }

    /**
     * Updates the state of the elements based on the current mouse position.
     *
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    protected void updateState(int mouseX, int mouseY) {
        var hit = hitTester.hitTest(eventRoot(), mouseX, mouseY);
        if (hit == null) {
            // Clear hovered state if no element is hit
            hoveredElement(null);
            return;
        }

        // Update hovered state
        hoveredElement(hoveredElement);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        // Update element states first
        updateState(mouseX, mouseY);
    }

    // Handle events

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        updateState(x, y);
        if (hoveredElement == null) return false;

        // Fire mouse click event to the hovered element
        var event = new MouseClickEvent(x, y, button);
        hoveredElement.events().fireEvent(event);
        return true;
    }
}
