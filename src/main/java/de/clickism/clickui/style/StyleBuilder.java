package de.clickism.clickui.style;

import de.clickism.clickui.UiColor;
import de.clickism.clickui.util.Self;

public interface StyleBuilder<S extends StyleBuilder<S>>
    extends Self<S> {

    Style style();

    default S backgroundColor(UiColor backgroundColor) {
        style().set(StyleProperty.BACKGROUND_COLOR, backgroundColor);
        return self();
    }

    default S alpha(float alpha) {
        style().set(StyleProperty.ALPHA, alpha);
        return self();
    }

    default S fontScale(float fontScale) {
        style().set(StyleProperty.FONT_SCALE, fontScale);
        return self();
    }

    default S overlayColor(UiColor overlayColor) {
        style().set(StyleProperty.OVERLAY_COLOR, overlayColor);
        return self();
    }

    // Borders

    default S borderColor(UiColor color) {
        style().update(StyleProperty.BORDER_COLOR, border ->
            border.withAllSides(old -> color));
        return self();
    }

    default S borderWidth(int width) {
        style().update(StyleProperty.BORDER_WIDTH, border ->
            border.withAllSides(old -> width));
        return self();
    }

    default S borderPosition(Border.Position position) {
        style().update(StyleProperty.BORDER_POSITION, border ->
            border.withAllSides(old -> position));
        return self();
    }

    default S borderColor(FourSided<UiColor> color) {
        style().set(StyleProperty.BORDER_COLOR, color);
        return self();
    }

    default S borderWidth(FourSided<Integer> width) {
        style().set(StyleProperty.BORDER_WIDTH, width);
        return self();
    }

    default S borderPosition(FourSided<Border.Position> position) {
        style().set(StyleProperty.BORDER_POSITION, position);
        return self();
    }
}
