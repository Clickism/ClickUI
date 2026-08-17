package de.clickism.clickui.style;

import org.jetbrains.annotations.Nullable;

import java.awt.*;

public final class ResolvedStyle implements Styleable<ResolvedStyle> {
    @Nullable Color background = null;
    @Nullable Color border = null;
    // TODO: Different border styles for each side
    // TODO: Border position (inside, outside, center)
    int borderWidth = 1;
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
        copy.borderWidth = this.borderWidth;
        copy.alpha = this.alpha;
        copy.fontScale = this.fontScale;
        return copy;
    }
}
