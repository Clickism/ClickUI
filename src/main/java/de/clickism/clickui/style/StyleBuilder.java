package de.clickism.clickui.style;

import de.clickism.clickui.UiColor;
import de.clickism.clickui.util.Self;

public interface StyleBuilder<S extends StyleBuilder<S>>
    extends Self<S> {

    Style style();

    default S textColor(UiColor textColor) {
        style().set(StyleProperty.TEXT_COLOR, textColor);
        return self();
    }

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

    default S borderColorTop(UiColor color) {
        style().update(StyleProperty.BORDER_COLOR, border ->
            border.withTop(color));
        return self();
    }

    default S borderColorBottom(UiColor color) {
        style().update(StyleProperty.BORDER_COLOR, border ->
            border.withBottom(color));
        return self();
    }

    default S borderColorLeft(UiColor color) {
        style().update(StyleProperty.BORDER_COLOR, border ->
            border.withLeft(color));
        return self();
    }

    default S borderColorRight(UiColor color) {
        style().update(StyleProperty.BORDER_COLOR, border ->
            border.withRight(color));
        return self();
    }

    default S borderWidthTop(int width) {
        style().update(StyleProperty.BORDER_WIDTH, border ->
            border.withTop(width));
        return self();
    }

    default S borderWidthBottom(int width) {
        style().update(StyleProperty.BORDER_WIDTH, border ->
            border.withBottom(width));
        return self();
    }

    default S borderWidthLeft(int width) {
        style().update(StyleProperty.BORDER_WIDTH, border ->
            border.withLeft(width));
        return self();
    }

    default S borderWidthRight(int width) {
        style().update(StyleProperty.BORDER_WIDTH, border ->
            border.withRight(width));
        return self();
    }

    default S borderPositionTop(Border.Position position) {
        style().update(StyleProperty.BORDER_POSITION, border ->
            border.withTop(position));
        return self();
    }

    default S borderPositionBottom(Border.Position position) {
        style().update(StyleProperty.BORDER_POSITION, border ->
            border.withBottom(position));
        return self();
    }

    default S borderPositionLeft(Border.Position position) {
        style().update(StyleProperty.BORDER_POSITION, border ->
            border.withLeft(position));
        return self();
    }

    default S borderPositionRight(Border.Position position) {
        style().update(StyleProperty.BORDER_POSITION, border ->
            border.withRight(position));
        return self();
    }
}
