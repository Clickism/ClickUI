package de.clickism.clickui.elements.input;

import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Point;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.util.Util;
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
            int start = highlightStart();
            int end = highlightEnd();
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
    private int highlightStart() {
        return Math.min(cursorPos, highlightPos);
    }

    /**
     * Gets the end index of the highlighted section of the text
     *
     * @return the end index of the highlighted text
     */
    private int highlightEnd() {
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
        // TODO: Check if visible
        return !this.disabled() && this.focused();
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

    /**
     * Returns the text to be displayed in the text box,
     * either the current value or the placeholder if the value is empty.
     *
     * @return the text to be displayed in the text box
     */
    protected String textToShow() {
        return value.isEmpty()
            ? placeholder
            : value;
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
        var time = System.currentTimeMillis();
        var blinking = time / 300 % 2 == 0; // Blink every 300 ms
        return listening() && !blinking;
    }

    protected boolean isHighlightVisible() {
        return highlightPos != cursorPos;
    }

    /**
     * Calculates the position where the text should be rendered within the text box.
     *
     * @return the position of the text within the text box
     */
    protected Point textPosition() {
        var bounds = bounds();
        var x = bounds.x();
        var y = bounds.y();
        var padding = padding();
        // Align
        var textHeight = Util.font().lineHeight;
        y += (bounds.height() - textHeight) / 2;
        y += 1; // Better visual alignment
        // Add padding
        x += padding.left();
        return new Point(x, y);
    }

    @Override
    public void render(RenderContext context) {
        // TODO: Refactor
        var textPos = textPosition();
        var text = textToShow();

        // Render text
        {
            var x = textPos.x();
            var y = textPos.y();
            var placeholder = isPlaceholderVisible();

            renderText(context, text, x, y, placeholder);
        }

        // Cursor
        if (isCursorVisible()) {
            boolean inline = cursorPos < text.length();
            var leftOfCursor = text.substring(0, cursorPos);

            var font = context.font();
            var x = textPos.x() + font.width(leftOfCursor);
            var y = textPos.y();
            renderCursor(context, x, y, inline);
        }

        // Highlight
        if (isHighlightVisible()) {
            // Calculate highlight start and end positions
            String before = text.substring(0, highlightStart());
            String highlighted = text.substring(highlightStart(), highlightEnd());

            var font = Util.font();

            var x = textPos.x() + font.width(before);
            var y = textPos.y();
            var width = font.width(highlighted);
            renderHighlight(context, x, y, width);
        }
    }

    protected abstract void renderText(
        RenderContext context,
        String text,
        int x,
        int y,
        boolean placeholder
    );

    protected abstract void renderCursor(
        RenderContext context,
        int x,
        int y,
        boolean inline
    );

    protected abstract void renderHighlight(
        RenderContext context,
        int x,
        int y,
        int width
    );
}
