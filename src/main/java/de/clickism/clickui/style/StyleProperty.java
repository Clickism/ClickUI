package de.clickism.clickui.style;

import de.clickism.clickui.UiColor;

/**
 * Represents a style property with a default value.
 *
 * @param <T> the type of the style property
 */
public interface StyleProperty<T> {
    // Default style properties
    StyleProperty<UiColor> TEXT_COLOR = of(UiColor.WHITE);
    StyleProperty<UiColor> BACKGROUND_COLOR = of(UiColor.TRANSPARENT);
    StyleProperty<Float> ALPHA = of(1.0f);
    StyleProperty<Float> FONT_SCALE = of(1.0f);
    StyleProperty<UiColor> OVERLAY_COLOR = of(UiColor.TRANSPARENT);
    // Border properties
    StyleProperty<FourSided<UiColor>> BORDER_COLOR = ofFourSided(UiColor.TRANSPARENT);
    StyleProperty<FourSided<Integer>> BORDER_WIDTH = ofFourSided(1);
    StyleProperty<FourSided<Border.Position>> BORDER_POSITION = ofFourSided(Border.Position.OUTSIDE);

    /**
     * Creates a new StyleProperty with the specified default value.
     *
     * @param defaultValue the default value of the style property
     * @param <T>          the type of the style property
     * @return a new StyleProperty instance
     */
    static <T> StyleProperty<T> of(T defaultValue) {
        return () -> defaultValue;
    }

    static <T> StyleProperty<FourSided<T>> ofFourSided(T defaultValue) {
        return () -> FourSided.of(defaultValue);
    }

    /**
     * Returns the default value of this style property.
     *
     * @return the default value
     */
    T defaultValue();
}
