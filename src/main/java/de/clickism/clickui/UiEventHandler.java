package de.clickism.clickui;

import de.clickism.clickui.event.EventState;
import de.clickism.clickui.event.HitTester;
import de.clickism.clickui.event.events.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * A screen that handles hovered elements and all events,
 * and propagates them to the element tree.
 */
public abstract class UiEventHandler extends Screen {
    private static final int DRAG_THRESHOLD = 5;
    /**
     * Keep track of the hovered element
     */
    private @Nullable UiElement<?> hoveredElement = null;
    /**
     * Keep track of the focused element
     */
    private @Nullable UiElement<?> focusedElement = null;
    /**
     * Keep track of the dragged element
     */
    private @Nullable UiElement<?> draggedElement = null;
    private double dragStartX = 0;
    private double dragStartY = 0;

    private final HitTester hitTester = new HitTester();

    private final UiElement<?> root;

    private final Set<Integer> pressedKeys = new HashSet<>();

    protected UiEventHandler(Component component, UiElement<?> root) {
        super(component);
        this.root = root;
    }

    /**
     * Updates the hovered element and its state.
     *
     * @param element the element that is currently hovered, or null if no element is hovered
     */
    private void hoveredElement(@Nullable UiElement<?> element) {
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
    protected @Nullable UiElement<?> hoveredElement() {
        return hoveredElement;
    }

    /**
     * Updates the hovered state of the elements based on the current mouse position.
     *
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    protected void updateHoverState(int mouseX, int mouseY) {
        var hit = hitTester.hitTest(root, mouseX, mouseY);
        if (hit == null) {
            // Clear hovered state if no element is hit
            hoveredElement(null);
            return;
        }

        // Send hover events
        var target = hit.target();
        if (hoveredElement != target) {
            // Mouse exit event
            if (hoveredElement != null) {
                hoveredElement.events().fireEvent(new MouseExitEvent(mouseX, mouseY, new EventState()));
            }
            // Mouse enter event
            target.events().fireEvent(new MouseEnterEvent(mouseX, mouseY, new EventState()));
        }

        // Update hovered state
        hoveredElement(target);
    }

    /**
     * Updates the focused state of the elements based on the currently focused element.
     *
     * @param x       the x-coordinate of the mouse
     * @param y       the y-coordinate of the mouse
     * @param element the element that is currently focused, or null if no element is focused
     */
    protected void updateFocusState(@Nullable UiElement<?> element, int x, int y) {
        if (focusedElement != null && focusedElement != element) {
            focusedElement.state().focused(false);
            // Send event
            focusedElement.propagateEventUp(new FocusExitEvent(x, y, new EventState()));
        }
        focusedElement = element;
        if (focusedElement != null) {
            focusedElement.state().focused(true);
            // Send event
            focusedElement.propagateEventUp(new FocusEnterEvent(x, y, new EventState()));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        // Update element states first
        updateHoverState(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        updateHoverState(x, y);
        updateFocusState(hoveredElement, x, y);
        if (hoveredElement == null) return false;
        if (hoveredElement.disabled()) return false;

        // Fire mouse click event to the hovered element
        var event = new MouseClickEvent(x, y, button, new EventState());
        hoveredElement.propagateEventUp(event);

        // Start dragging
        draggedElement = hoveredElement;
        dragStartX = mouseX;
        dragStartY = mouseY;
        var dragEvent = new DragStartEvent(x, y, button, new EventState());
        draggedElement.propagateEventUp(dragEvent);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        updateHoverState(x, y);

        boolean hovered = hoveredElement != null && !hoveredElement.disabled();
        if (hovered) {
            // Fire mouse release event to the hovered element
            var event = new MouseReleaseEvent(x, y, button, new EventState());
            hoveredElement.propagateEventUp(event);
        }

        // End dragging
        if (draggedElement != null) {
            var dragEndEvent = new DragEndEvent(dragStartX, dragStartY, x, y, button, new EventState());
            draggedElement.propagateEventUp(dragEndEvent);
            draggedElement = null;
        }
        return hovered;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        updateHoverState(x, y);
        if (hoveredElement == null) return false;
        // Fire to all
        var event = new MouseScrollEvent(x, y, delta, new EventState());
        hoveredElement.propagateEventUp(event);

        return true;
    }

    @Override
    public boolean keyPressed(int code, int scanCode, int modifiers) {
        if (super.keyPressed(code, scanCode, modifiers)) return true;

        if (!pressedKeys.add(code)) {
            // Key is already pressed, ignore repeat and call held event
            var event = new KeyHeldEvent(code, scanCode, modifiers, new EventState());
            root.propagateEventDown(event);
            return false;
        }

        // Fire to all
        var event = new KeyPressEvent(code, scanCode, modifiers, new EventState());
        root.propagateEventDown(event);

        return false;
    }

    @Override
    public boolean keyReleased(int code, int scanCode, int modifiers) {
        pressedKeys.remove(code);

        // Fire to all
        var event = new KeyReleaseEvent(code, scanCode, modifiers, new EventState());
        root.propagateEventDown(event);

        return super.keyReleased(code, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (super.charTyped(character, modifiers)) return true;
        // Fire to all
        var event = new CharTypeEvent(character, modifiers, new EventState());
        root.propagateEventDown(event);

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        updateHoverState(x, y);
        if (draggedElement == null) return false;
        if (draggedElement.disabled()) return false;

        // Fire mouse drag event to the dragged element
        var event = new DragEvent(dragStartX, dragStartY, mouseX, mouseY, dragX, dragY, button, new EventState());
        draggedElement.propagateEventUp(event);

        return true;
    }
}
