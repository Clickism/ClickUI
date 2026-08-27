package de.clickism.clickui.layout;

/**
 * Represents the layout information for a UI element.
 */
public class Layout implements Layoutable<Layout> {
    Axis axis = Axis.VERTICAL;
    Sizing width = Sizing.fit();
    Sizing height = Sizing.fit();
    Padding padding = Padding.ZERO;
    int childGap = 0;
    boolean wrapChildren = false;
    Align mainAlign = Align.START;
    Align crossAlign = Align.START;
    // TODO: Don't count elements with ABSOLUTE sizing in the gap calculation
    Positioning positioning = Positioning.layout();

    @Override
    public Layout layout() {
        return this;
    }
}
