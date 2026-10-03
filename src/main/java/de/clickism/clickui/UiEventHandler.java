package de.clickism.clickui;

import com.mojang.blaze3d.platform.InputConstants;
import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventState;
import de.clickism.clickui.event.HitTester;
import de.clickism.clickui.event.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

//? if >= 26.1 {
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.CharacterEvent;
//?}

/**
 * A screen that handles hovered elements and all events,
 * and propagates them to the element tree.
 */
public abstract class UiEventHandler extends Screen {
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

    /**
     * Constructs a new UiEventHandler with the specified title and root element.
     *
     * @param component the title of the screen
     * @param root      the root element of the UI hierarchy
     */
    protected UiEventHandler(Component component, UiElement<?> root) {
        super(component);
        this.root = root;
        // Handle SDL input
        //? if >=26.3
        Minecraft.getInstance().onTextInputFocusChange(this, true);
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
                hoveredElement.events().fireEvent(
                    new MouseExitEvent(hoveredElement, mouseX, mouseY, new EventState())
                );
            }
            // Mouse enter event
            target.events().fireEvent(
                new MouseEnterEvent(hoveredElement, mouseX, mouseY, new EventState())
            );
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
            focusedElement.propagateEventUp(new FocusExitEvent(element, x, y, new EventState()));
        }
        focusedElement = element;
        if (focusedElement != null) {
            focusedElement.state().focused(true);
            // Send event
            focusedElement.propagateEventUp(new FocusEnterEvent(element, x, y, new EventState()));
        }
    }

    //~ if < 26.1 'extractRenderState' -> 'render' {

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, delta);
        // Update element states first
        updateHoverState(mouseX, mouseY);
    }

    //~}

    /**
     * Fires a mouse event to the currently hovered element, if any.
     *
     * @param event the mouse event to fire
     * @return true if the event was fired to a hovered element, false otherwise
     */
    private boolean fireMouseEvent(Event event) {
        if (hoveredElement == null || hoveredElement.disabled()) return false;
        // Fire event to the hovered element
        hoveredElement.propagateEventUp(event);
        root.propagateEventDownGlobal(event);
        return true;
    }

    /**
     * Fires a key event to the currently focused element, if any.
     *
     * @param event the key event to fire
     * @return true if the event was fired to a focused element, false otherwise
     */
    private boolean fireKeyEvent(Event event) {
        if (focusedElement == null || focusedElement.disabled()) return false;
        // Fire event to the focused element
        focusedElement.propagateEventUp(event);
        root.propagateEventDownGlobal(event);
        return true;
    }

    //~ if < 26.1 'MouseButtonEvent screenEvent' -> 'double mouseX, double mouseY, int button'
    //~ if >= 26.1 'unwrapEvent();' -> 'int mouseX = (int) screenEvent.x(); int mouseY = (int) screenEvent.y(); int button = screenEvent.button();' {

    @Override
    public boolean mouseClicked(
        MouseButtonEvent screenEvent
        //? if >= 26.1
        ,boolean doubleClick
    ) {
        int mouseX = (int) screenEvent.x(); int mouseY = (int) screenEvent.y(); int button = screenEvent.button();

        updateFocusState(hoveredElement, (int) mouseX, (int) mouseY);

        var event = new MouseClickEvent(hoveredElement, (int) mouseX, (int) mouseY, button, new EventState());
        if (fireMouseEvent(event)) {
            if (hoveredElement == null) return true;
            // Start dragging
            draggedElement = hoveredElement;
            dragStartX = mouseX;
            dragStartY = mouseY;
            var dragEvent = new DragStartEvent(hoveredElement, mouseX, mouseY, button, new EventState());
            draggedElement.propagateEventUp(dragEvent);
            root.propagateEventDownGlobal(dragEvent);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent screenEvent) {
        int mouseX = (int) screenEvent.x(); int mouseY = (int) screenEvent.y(); int button = screenEvent.button();

        var event = new MouseReleaseEvent(hoveredElement, (int) mouseX, (int) mouseY, button, new EventState());
        var fired = fireMouseEvent(event);

        // End dragging
        if (draggedElement != null) {
            var dragEndEvent = new DragEndEvent(draggedElement, dragStartX, dragStartY, mouseX, mouseY, button, new EventState());
            draggedElement.propagateEventUp(dragEndEvent);
            root.propagateEventDownGlobal(dragEndEvent);
            draggedElement = null;
        }

        return fired;
    }

    @Override
    public boolean mouseScrolled(
        double mouseX, double mouseY,
        //? if >= 1.21.1
        double horizontalDelta,
        double delta
    ) {
        // Fire to all
        var event = new MouseScrollEvent(hoveredElement, (int) mouseX, (int) mouseY, delta, new EventState());
        return fireMouseEvent(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent screenEvent, double dragX, double dragY) {
        int mouseX = (int) screenEvent.x(); int mouseY = (int) screenEvent.y(); int button = screenEvent.button();

        if (draggedElement == null || draggedElement.disabled()) return false;
        // Fire mouse drag event to the dragged element
        var event = new DragEvent(
            draggedElement,
            dragStartX,
            dragStartY,
            mouseX,
            mouseY,
            dragX,
            dragY,
            button,
            new EventState()
        );
        draggedElement.propagateEventUp(event);
        root.propagateEventDownGlobal(event);
        return true;
    }
    
    //~}

    //~ if < 26.1 'KeyEvent screenEvent' -> 'int code, int scanCode, int modifiers'
    //~ if >= 26.1 && <26.3 'unwrapEvent();' -> 'int code = screenEvent.key(); int scanCode = screenEvent.scancode(); int modifiers = screenEvent.modifiers();'
    //~ if >= 26.3 'unwrapEvent();' -> 'int code = screenEvent.key(); int scanCode = screenEvent.keycode(); int modifiers = screenEvent.modifiers();'
    //~ if < 26.1 '(screenEvent)' -> '(code, scanCode, modifiers)' {

    @Override
    public boolean keyPressed(KeyEvent screenEvent) {
        int code = screenEvent.key(); int scanCode = screenEvent.keycode(); int modifiers = screenEvent.modifiers();

        if (super.keyPressed(screenEvent)) return true;

        if (!pressedKeys.add(code)) {
            // Key is already pressed, ignore repeat and call held event
            var event = new KeyHeldEvent(focusedElement, code, scanCode, modifiers, new EventState());
            return fireKeyEvent(event);
        }
        var event = new KeyPressEvent(focusedElement, code, scanCode, modifiers, new EventState());
        return fireKeyEvent(event);
    }

    @Override
    public boolean keyReleased(KeyEvent screenEvent) {
        int code = screenEvent.key(); int scanCode = screenEvent.keycode(); int modifiers = screenEvent.modifiers();

        pressedKeys.remove(code);
        var event = new KeyReleaseEvent(focusedElement, code, scanCode, modifiers, new EventState());
        fireKeyEvent(event);

        return super.keyReleased(screenEvent);
    }

    //~}

    //~ if < 26.1 'CharacterEvent screenEvent' -> 'char character, int modifiers'
    //~ if >= 26.1 'unwrapEvent();' -> 'char character = (char) screenEvent.codepoint(); int modifiers = 0;'
    //~ if < 26.1 '(screenEvent)' -> '(character, modifiers)' {

    @Override
    public boolean charTyped(CharacterEvent screenEvent) {
        char character = (char) screenEvent.codepoint(); int modifiers = 0;

        if (super.charTyped(screenEvent)) return true;

        var event = new CharTypeEvent(focusedElement, character, modifiers, new EventState());
        fireKeyEvent(event);

        return false;
    }

    //~}

    private void unwrapEvent() {
        // Nothing, used as placeholder for versioning
    }
}
