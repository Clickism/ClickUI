package de.clickism.clickui.layout;

import de.clickism.clickui.LayoutAxis;
import de.clickism.clickui.Padding;
import de.clickism.clickui.Sizing;

public interface Layoutable<T extends Layoutable<T>> {
    Layout layout();

    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }

    default LayoutAxis axis() {
        return layout().axis;
    }

    default T axis(LayoutAxis axis) {
        layout().axis = axis;
        return self();
    }

    default T vertical() {
        layout().axis = LayoutAxis.VERTICAL;
        return self();
    }

    default T horizontal() {
        layout().axis = LayoutAxis.HORIZONTAL;
        return self();
    }

    default Sizing width() {
        return layout().width;
    }

    default T width(Sizing width) {
        layout().width = width;
        return self();
    }

    default T width(int width) {
        layout().width = Sizing.fixed(width);
        return self();
    }

    default Sizing height() {
        return layout().height;
    }

    default T height(Sizing height) {
        layout().height = height;
        return self();
    }

    default T height(int height) {
        layout().height = Sizing.fixed(height);
        return self();
    }

    default Padding padding() {
        return layout().padding;
    }

    default T padding(Padding padding) {
        layout().padding = padding;
        return self();
    }

    default T padding(int padding) {
        layout().padding = Padding.uniform(padding);
        return self();
    }

    default int childGap() {
        return layout().childGap;
    }

    default T childGap(int childGap) {
        layout().childGap = childGap;
        return self();
    }
}
