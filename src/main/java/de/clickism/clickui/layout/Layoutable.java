package de.clickism.clickui.layout;

import de.clickism.clickui.util.Self;

public interface Layoutable<S extends Layoutable<S>>
    extends Self<S> {
    /**
     * Returns the layout information for the UI element.
     *
     * @return the layout information
     */
    Layout layout();

    default Axis axis() {
        return layout().axis;
    }

    default S axis(Axis axis) {
        layout().axis = axis;
        return self();
    }

    default S vertical() {
        layout().axis = Axis.VERTICAL;
        return self();
    }

    default S horizontal() {
        layout().axis = Axis.HORIZONTAL;
        return self();
    }

    default Sizing width() {
        return layout().width;
    }

    default S width(Sizing width) {
        layout().width = width;
        return self();
    }

    default S width(int width) {
        layout().width = Sizing.fixed(width);
        return self();
    }

    default S growWidth() {
        layout().width = Sizing.grow();
        return self();
    }

    default S minWidth(int minWidth) {
        layout().width = layout().width.min(minWidth);
        return self();
    }

    default S maxWidth(int maxWidth) {
        layout().width = layout().width.max(maxWidth);
        return self();
    }

    default Sizing height() {
        return layout().height;
    }

    default S height(Sizing height) {
        layout().height = height;
        return self();
    }

    default S height(int height) {
        layout().height = Sizing.fixed(height);
        return self();
    }

    default S growHeight() {
        layout().height = Sizing.grow();
        return self();
    }

    default S minHeight(int minHeight) {
        layout().height = layout().height.min(minHeight);
        return self();
    }

    default S maxHeight(int maxHeight) {
        layout().height = layout().height.max(maxHeight);
        return self();
    }

    /**
     * Sets both the width and height of the element to a fixed size.
     *
     * @param size the fixed size to set for both width and height
     * @return the current instance for method chaining
     */
    default S size(int size) {
        layout().width = Sizing.fixed(size);
        layout().height = Sizing.fixed(size);
        return self();
    }

    default Sizing mainSizing() {
        return layout().axis.isHorizontal()
            ? layout().width
            : layout().height;
    }

    default Sizing crossSizing() {
        return layout().axis.isHorizontal()
            ? layout().height
            : layout().width;
    }

    default S grow() {
        layout().width = Sizing.grow();
        layout().height = Sizing.grow();
        return self();
    }

    default Padding padding() {
        return layout().padding;
    }

    default S padding(int top, int right, int bottom, int left) {
        layout().padding = new Padding(top, right, bottom, left);
        return self();
    }

    default S padding(int vertical, int horizontal) {
        layout().padding = new Padding(vertical, horizontal, vertical, horizontal);
        return self();
    }

    default S padding(Padding padding) {
        layout().padding = padding;
        return self();
    }

    default S padding(int padding) {
        layout().padding = Padding.create(padding);
        return self();
    }

    default int childGap() {
        return layout().childGap;
    }

    default S childGap(int childGap) {
        layout().childGap = childGap;
        return self();
    }

    default Align mainAlign() {
        return layout().mainAlign;
    }

    default Align crossAlign() {
        return layout().crossAlign;
    }

    default S mainAlign(Align align) {
        layout().mainAlign = align;
        return self();
    }

    default S crossAlign(Align align) {
        layout().crossAlign = align;
        return self();
    }

    default S alignCenter() {
        layout().mainAlign = Align.CENTER;
        layout().crossAlign = Align.CENTER;
        return self();
    }

    default Positioning positioning() {
        return layout().positioning;
    }

    // TODO: Test different positioning modes and their effects on layout
    default S positioning(Positioning positioning) {
        layout().positioning = positioning;
        return self();
    }

    default S relative(int x, int y) {
        layout().positioning = Positioning.relative(x, y);
        return self();
    }

    default S absolute(int x, int y) {
        layout().positioning = Positioning.absolute(x, y);
        return self();
    }
}
