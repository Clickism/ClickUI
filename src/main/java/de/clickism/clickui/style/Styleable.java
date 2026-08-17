package de.clickism.clickui.style;

import de.clickism.clickui.util.Self;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public interface Styleable<S extends Styleable<S>> extends Self<S> {

    /**
     * Returns the resolved style for the UI element.
     *
     * @return the resolved style
     */
    ResolvedStyle style();

    default S background(@Nullable Color background) {
        style().background = background;
        return self();
    }

    default S border(@Nullable Color border) {
        style().border = border;
        return self();
    }

    default S borderWidth(int borderWidth) {
        style().borderWidth = borderWidth;
        return self();
    }

    default S alpha(float alpha) {
        style().alpha = alpha;
        return self();
    }

    default S fontScale(float fontScale) {
        style().fontScale = fontScale;
        return self();
    }
}
