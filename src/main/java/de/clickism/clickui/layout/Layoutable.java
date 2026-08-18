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

    default LayoutAxis axis() {
        return layout().axis;
    }

    default S axis(LayoutAxis axis) {
        layout().axis = axis;
        return self();
    }

    default S vertical() {
        layout().axis = LayoutAxis.VERTICAL;
        return self();
    }

    default S horizontal() {
        layout().axis = LayoutAxis.HORIZONTAL;
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
        layout().padding = Padding.uniform(padding);
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
