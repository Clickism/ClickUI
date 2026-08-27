package de.clickism.clickui.style;

import de.clickism.clickui.UiColor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

// TODO: Refactor style to be more flexible, and be layered with nullable props maybe
public final class ResolvedStyle implements Styleable<ResolvedStyle> {
    @Nullable UiColor background = null;
    @Nullable UiColor border = null;
    // TODO: Different border styles for each side
    // TODO: Border position (inside, outside, center)
    int borderWidth = 1;
    BorderPosition borderPosition = BorderPosition.OUTSIDE;
    float alpha = 1.0f;
    float fontScale = 1.0f;

    @Override
    public ResolvedStyle style() {
        return this;
    }

    public ResolvedStyle copy() {
        var copy = new ResolvedStyle();
        copy.background = this.background;
        copy.border = this.border;
        copy.borderPosition = this.borderPosition;
        copy.borderWidth = this.borderWidth;
        copy.alpha = this.alpha;
        copy.fontScale = this.fontScale;
        return copy;
    }

    public @Nullable UiColor background() {
        return background;
    }

    public @Nullable UiColor border() {
        return border;
    }

    public int borderWidth() {
        return borderWidth;
    }

    public BorderPosition borderPosition() {
        return borderPosition;
    }

    public float alpha() {
        return alpha;
    }

    public float fontScale() {
        return fontScale;
    }
}
