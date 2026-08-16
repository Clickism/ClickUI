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

    default @Nullable Color background() {
        return style().background;
    }

    default S background(@Nullable Color background) {
        style().background = background;
        return self();
    }

    default @Nullable Color border() {
        return style().border;
    }

    default S border(@Nullable Color border) {
        style().border = border;
        return self();
    }

    default int borderWidth() {
        return style().borderWidth;
    }

    default S borderWidth(int borderWidth) {
        style().borderWidth = borderWidth;
        return self();
    }

    default float alpha() {
        return style().alpha;
    }

    default S alpha(float alpha) {
        style().alpha = alpha;
        return self();
    }
}
