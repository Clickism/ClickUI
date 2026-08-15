package de.clickism.clickui.layout;

import de.clickism.clickui.LayoutAxis;
import de.clickism.clickui.Padding;
import de.clickism.clickui.Self;
import de.clickism.clickui.Sizing;

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

    default Padding padding() {
        return layout().padding;
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
}
