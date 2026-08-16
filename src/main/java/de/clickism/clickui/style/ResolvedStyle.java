package de.clickism.clickui.style;

import org.jetbrains.annotations.Nullable;

import java.awt.*;

public final class ResolvedStyle implements Styleable<ResolvedStyle> {
    @Nullable Color background = null;
    @Nullable Color outline = null;
    int outlineWidth = 0;
    float alpha = 1.0f;

    @Override
    public ResolvedStyle style() {
        return this;
    }

    public ResolvedStyle copy() {
        var copy = new ResolvedStyle();
        copy.background = this.background;
        copy.outline = this.outline;
        copy.outlineWidth = this.outlineWidth;
        copy.alpha = this.alpha;
        return copy;
    }
}
