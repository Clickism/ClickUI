package de.clickism.clickui.layout;

import de.clickism.clickui.*;

/**
 * Represents the layout information for a UI element.
 */
public class Layout implements Layoutable<Layout> {
    LayoutAxis axis = LayoutAxis.VERTICAL;
    Sizing width = Sizing.fit();
    Sizing height = Sizing.fit();
    Padding padding = Padding.ZERO;
    int childGap = 0;
    Align mainAlign = Align.START;
    Align crossAlign = Align.START;

    @Override
    public Layout layout() {
        return this;
    }
}
