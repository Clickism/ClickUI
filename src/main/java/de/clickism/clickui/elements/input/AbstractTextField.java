package de.clickism.clickui.elements.input;

import de.clickism.clickui.Element;
import de.clickism.clickui.render.RenderContext;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An abstract class representing a text field UI element.
 * It provides basic functionality for text input, cursor movement, and text editing.
 *
 * @param <S> the type of the subclass extending this abstract class
 */
// TODO: Suggestions
public abstract class AbstractTextField<S extends AbstractTextField<S>>
    extends Element<S> {

    protected String value = "";
    protected String placeholder = "";

    protected int cursorPos = 0;
    protected int highlightPos = 0;

    protected int displayPos;
    // TODO: Max length, filter, etc.

    protected int tick = 0;

    protected List<Consumer<String>> listeners = new ArrayList<>();

    /*+
     * Constructs a new AbstractTextField instance.
     */
    public AbstractTextField() {
        // Register key press event handler
        this.onKeyPress(event -> {
            if (!listening()) return;
            handleKeyPress(event.code());
        });
        // Register character typed event handler
        this.onCharTyped(event -> {
            if (!listening()) return;
            if (!SharedConstants.isAllowedChatCharacter(event.character())) return;
            // Insert text
            insertText(Character.toString(event.character()));
        });
    }

    /**
     * Set the text value of the text box.
     *
     * @param value the text value to set
     */
    public void value(String value) {
        this.value = value;
        setValidCursor(value.length());
        highlightPos = cursorPos;
    }

    /**
     * Get the current text value of the text box.
     *
     * @return the current text value of the text box
     */
    public String value() {
        return this.value;
    }

    /**
     * Gets the current text or the placeholder if the text is empty.
     * Should be used for diplaying the text
     *
     * @return the current text or the placeholder if the text is empty
     */
    public String valueOrPlaceholder() {
        return this.value.isEmpty()
            ? this.placeholder
            : this.value;
    }

    protected void triggerValueChanged() {
        for (var listener : listeners) {
            listener.accept(value);
        }
    }

    /**
     * Insert text at the current cursor position.
     *
     * @param string the text to insert
     */
    public void insertText(String string) {
        if (!listening()) return;
        string = filterInput(string);
        if (highlightPos != cursorPos) {
            // Remove highlighted text before inserting
            int start = selectionStart();
            int end = selectionEnd();
            value = value.substring(0, start) + value.substring(end);
            cursorPos = start;
            highlightPos = cursorPos;
        }
        // Insert text
        value = value.substring(0, cursorPos) + string + value.substring(cursorPos);
        setValidCursor(cursorPos + string.length());
        highlightPos = cursorPos;
        triggerValueChanged();
    }

    /**
     * Gets the highlighted section of the text
     *
     * @return the highlighted text
     */
    public String highlightedText() {
        int start = Math.min(cursorPos, highlightPos);
        int end = Math.max(cursorPos, highlightPos);
        return value.substring(start, end);
    }

    /**
     * Gets the start index of the highlighted section of the text
     *
     * @return the start index of the highlighted text
     */
    private int selectionStart() {
        return Math.min(cursorPos, highlightPos);
    }

    /**
     * Gets the end index of the highlighted section of the text
     *
     * @return the end index of the highlighted text
     */
    private int selectionEnd() {
        return Math.max(cursorPos, highlightPos);
    }

    /**
     * Deletes text in the given direction (positive for forward, negative for backward)
     * If text is highlighted, it will be removed instead.
     *
     * @param direction the direction to delete in, positive or negative
     */
    public void deleteText(int direction) {
        if (!listening()) return;
        if (direction == 0) return;
        // If text is highlighted, remove it instead
        if (highlightPos != cursorPos) {
            insertText("");
            return;
        }
        // Delete in direction
        if (direction > 0) {
            // Delete forward
            int end = Math.min(value.length(), cursorPos + direction);
            value = value.substring(0, cursorPos) + value.substring(end);
            // Cursor stays the same
        } else {
            // Delete backward
            int start = Math.max(0, cursorPos + direction);
            value = value.substring(0, start) + value.substring(cursorPos);
            cursorPos = start;
            highlightPos = cursorPos;
        }
        triggerValueChanged();
    }

    /**
     * Gets the position of the next word in the given direction (positive for forward, negative for backward).
     *
     * @param direction the direction to move in, positive or negative
     * @return the position of the next word in the given direction
     */
    private int wordPosition(int direction) {
        int pos = cursorPos;
        if (direction > 0) {
            // Skip current word
            while (pos < value.length() && !Character.isWhitespace(value.charAt(pos))) {
                pos++;
            }
            // skip spaces
            while (pos < value.length() && Character.isWhitespace(value.charAt(pos))) {
                pos++;
            }
        } else {
            // Skip spaces
            while (pos > 0 && Character.isWhitespace(value.charAt(pos - 1))) {
                pos--;
            }
            // Skip word
            while (pos > 0 && !Character.isWhitespace(value.charAt(pos - 1))) {
                pos--;
            }
        }
        return pos;
    }

    /**
     * Moves the cursor to a valid position in the given direction
     * (positive for forward, negative for backward).
     *
     * @param direction the direction to move in, positive or negative
     */
    private void moveCursor(int direction) {
        if (Screen.hasControlDown()) {
            // Move to next word
            cursorPos = wordPosition(direction);
        } else {
            // Move by one character
            cursorPos = Mth.clamp(cursorPos + direction, 0, value.length());
        }
        if (!Screen.hasShiftDown()) {
            highlightPos = cursorPos;
        }
    }

    /**
     * Sets the cursor position to a valid position and clamps if needed.
     *
     * @param pos the position to set the cursor to
     */
    private void setValidCursor(int pos) {
        this.cursorPos = Mth.clamp(pos, 0, value.length());
    }

    /**
     * Whether the text box is currently focused, editable and listening for input.
     *
     * @return true if the text box is focused and editable, false otherwise
     */
    public boolean listening() {
        return !this.disabled();
        // TODO: Add focus check when focus is implemented
//        return this.visible && this.isFocused() && this.editable;
    }

    /**
     * Filters the input string before inserting it into the text box.
     *
     * @param input the input string to filter
     * @return the filtered string
     */
    protected String filterInput(String input) {
        // TODO: Make this customizable
        return SharedConstants.filterText(input);
    }

    protected String textToShow() {
        return value.isEmpty() ? placeholder : value;
    }

    /**
     * Handles key press events for the text box, including text editing and navigation.
     *
     * @param code the key code of the pressed key
     */
    private void handleKeyPress(int code) {
        if (Screen.isSelectAll(code)) {
            // Move cursor to the end
            cursorPos = value.length();
            highlightPos = 0; // Highlight from start to end
            return;
        }
        if (Screen.isCopy(code)) {
            // Copy highlighted text to clipboard
            var keyboard = Minecraft.getInstance().keyboardHandler;
            keyboard.setClipboard(highlightedText());
            return;
        }
        if (Screen.isPaste(code)) {
            // Paste text from clipboard
            var keyboard = Minecraft.getInstance().keyboardHandler;
            insertText(keyboard.getClipboard());
            return;
        }
        if (Screen.isCut(code)) {
            // Copy highlighted text to clipboard and remove it from the value
            var keyboard = Minecraft.getInstance().keyboardHandler;
            keyboard.setClipboard(highlightedText());
            insertText("");
            return;
        }
        // Other keys
        switch (code) {
            case GLFW.GLFW_KEY_BACKSPACE -> {
                deleteText(-1);
            }
            case GLFW.GLFW_KEY_DELETE -> {
                deleteText(1);
            }
            case GLFW.GLFW_KEY_LEFT -> {
                moveCursor(-1);
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                moveCursor(1);
            }
            case GLFW.GLFW_KEY_HOME -> {
                cursorPos = 0;
                if (!Screen.hasShiftDown()) {
                    highlightPos = cursorPos;
                }
            }
            case GLFW.GLFW_KEY_END -> {
                cursorPos = value.length();
                if (!Screen.hasShiftDown()) {
                    highlightPos = cursorPos;
                }
            }
        }
    }

    protected boolean isPlaceholderVisible() {
        return value.isEmpty() && !placeholder.isEmpty() && !listening();
    }

    protected boolean isCursorVisible() {
        // Blink every 6 ticks
        var blinking = tick / 6 % 2 == 0;
        return listening() && !blinking;
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
    }

    @Override
    public void render(RenderContext context) {
        renderBackground(context);
        if (isPlaceholderVisible()) {
            renderPlaceholder(context);
        } else {
            renderText(context);
        }
        if (isCursorVisible()) {
            renderCursor(context);
        }
        renderHighlight(context);
    }

    protected abstract void renderBackground(RenderContext context);

    protected abstract void renderText(RenderContext context);

    protected abstract void renderPlaceholder(RenderContext context);

    protected abstract void renderCursor(RenderContext context);

    protected abstract void renderHighlight(RenderContext context);
}
