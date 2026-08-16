package de.clickism.clickui.style;

import de.clickism.clickui.Self;
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

    default @Nullable Color outline() {
        return style().outline;
    }

    default S outline(@Nullable Color outline) {
        style().outline = outline;
        return self();
    }

    default int outlineWidth() {
        return style().outlineWidth;
    }

    default S outlineWidth(int outlineWidth) {
        style().outlineWidth = outlineWidth;
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
