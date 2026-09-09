package de.clickism.clickui;

import java.awt.*;

/**
 * Represents a color in the UI system.
 * Meant to provide lots of useful colors and color manipulation methods.
 */
public interface UiColor {

    // ----------------
    // Basic colors
    // ----------------

    UiColor TRANSPARENT = rgba(0, 0, 0, 0.0f);

    UiColor BLACK = rgb(0, 0, 0);
    UiColor WHITE = rgb(255, 255, 255);
    UiColor GRAY = rgb(128, 128, 128);
    UiColor LIGHT_GRAY = rgb(192, 192, 192);
    UiColor DARK_GRAY = rgb(64, 64, 64);

    UiColor RED = rgb(255, 0, 0);
    UiColor GREEN = rgb(0, 255, 0);
    UiColor BLUE = rgb(0, 0, 255);

    UiColor YELLOW = rgb(255, 255, 0);
    UiColor CYAN = rgb(0, 255, 255);
    UiColor MAGENTA = rgb(255, 0, 255);

    // -------------
    // Common colors
    // -------------

    UiColor ORANGE = rgb(255, 165, 0);
    UiColor PINK = rgb(255, 192, 203);
    UiColor PURPLE = rgb(128, 0, 128);
    UiColor VIOLET = rgb(238, 130, 238);

    UiColor BROWN = rgb(165, 42, 42);
    UiColor BEIGE = rgb(245, 245, 220);
    UiColor TAN = rgb(210, 180, 140);

    UiColor LIME = rgb(50, 205, 50);
    UiColor OLIVE = rgb(128, 128, 0);
    UiColor TEAL = rgb(0, 128, 128);

    UiColor NAVY = rgb(0, 0, 128);
    UiColor AQUA = rgb(0, 255, 255);
    UiColor MAROON = rgb(128, 0, 0);
    UiColor FUCHSIA = rgb(255, 0, 255);

    // ---------------------------
    // Black with alpha variations
    // ---------------------------

    UiColor BLACK_A90 = rgba(0, 0, 0, 0.9f);
    UiColor BLACK_A80 = rgba(0, 0, 0, 0.8f);
    UiColor BLACK_A70 = rgba(0, 0, 0, 0.7f);
    UiColor BLACK_A60 = rgba(0, 0, 0, 0.6f);
    UiColor BLACK_A50 = rgba(0, 0, 0, 0.5f);
    UiColor BLACK_A40 = rgba(0, 0, 0, 0.4f);
    UiColor BLACK_A30 = rgba(0, 0, 0, 0.3f);
    UiColor BLACK_A20 = rgba(0, 0, 0, 0.2f);
    UiColor BLACK_A10 = rgba(0, 0, 0, 0.1f);

    // ---------------------------
    // White with alpha variations
    // ---------------------------

    UiColor WHITE_A90 = rgba(255, 255, 255, 0.9f);
    UiColor WHITE_A80 = rgba(255, 255, 255, 0.8f);
    UiColor WHITE_A70 = rgba(255, 255, 255, 0.7f);
    UiColor WHITE_A60 = rgba(255, 255, 255, 0.6f);
    UiColor WHITE_A50 = rgba(255, 255, 255, 0.5f);
    UiColor WHITE_A40 = rgba(255, 255, 255, 0.4f);
    UiColor WHITE_A30 = rgba(255, 255, 255, 0.3f);
    UiColor WHITE_A20 = rgba(255, 255, 255, 0.2f);
    UiColor WHITE_A10 = rgba(255, 255, 255, 0.1f);

    // ----------------
    // Minecraft Colors
    // ----------------

    UiColor MC_BLACK = rgb(0x000000);
    UiColor MC_DARK_BLUE = rgb(0x0000AA);
    UiColor MC_DARK_GREEN = rgb(0x00AA00);
    UiColor MC_DARK_AQUA = rgb(0x00AAAA);
    UiColor MC_DARK_RED = rgb(0xAA0000);
    UiColor MC_DARK_PURPLE = rgb(0xAA00AA);
    UiColor MC_GOLD = rgb(0xFFAA00);
    UiColor MC_GRAY = rgb(0xAAAAAA);
    UiColor MC_DARK_GRAY = rgb(0x555555);
    UiColor MC_BLUE = rgb(0x5555FF);
    UiColor MC_GREEN = rgb(0x55FF55);
    UiColor MC_AQUA = rgb(0x55FFFF);
    UiColor MC_RED = rgb(0xFF5555);
    UiColor MC_LIGHT_PURPLE = rgb(0xFF55FF);
    UiColor MC_YELLOW = rgb(0xFFFF55);
    UiColor MC_WHITE = rgb(0xFFFFFF);

    /**
     * Implementation of UiColor that holds a color value.
     *
     * @param color the color value in ARGB format
     */
    record Impl(int color) implements UiColor {}

    /**
     * Returns the color value in ARGB format.
     *
     * @return the color value in ARGB format
     */
    int color();

    default int red() {
        return (color() >> 16) & 0xFF;
    }

    default int green() {
        return (color() >> 8) & 0xFF;
    }

    default int blue() {
        return color() & 0xFF;
    }

    default float alpha() {
        return ((color() >> 24) & 0xFF) / 255.0f;
    }

    default float luminance() {
        return (0.2126f * red()
                + 0.7152f * green()
                + 0.0722f * blue()) / 255.0f;
    }

    default UiColor pickBetterContrasting(UiColor color1, UiColor color2) {
        return contrastRatio(color1) >= contrastRatio(color2)
            ? color1
            : color2;
    }

    default float contrastRatio(UiColor other) {
        float l1 = luminance();
        float l2 = other.luminance();

        float lighter = Math.max(l1, l2);
        float darker = Math.min(l1, l2);

        return (lighter + 0.05f) / (darker + 0.05f);
    }

    default UiColor alpha(float a) {
        return rgba(red(), green(), blue(), a);
    }

    default UiColor darken(float factor) {
        int r = (int) (red() * factor);
        int g = (int) (green() * factor);
        int b = (int) (blue() * factor);
        return rgb(r, g, b);
    }

    default UiColor lighten(float factor) {
        int r = (int) (red() + (255 - red()) * factor);
        int g = (int) (green() + (255 - green()) * factor);
        int b = (int) (blue() + (255 - blue()) * factor);
        return rgb(r, g, b);
    }

    default UiColor blend(UiColor other, float factor) {
        int r = (int) (red() + (other.red() - red()) * factor);
        int g = (int) (green() + (other.green() - green()) * factor);
        int b = (int) (blue() + (other.blue() - blue()) * factor);
        float a = alpha() + (other.alpha() - alpha()) * factor;
        return rgba(r, g, b, a);
    }

    default UiColor multiplyAlpha(float factor) {
        return rgba(red(), green(), blue(), alpha() * factor);
    }

    default Color toColor() {
        return new Color(color(), true);
    }

    static UiColor rgba(int r, int g, int b, float a) {
        int alpha = (int) (a * 255.0f);
        int color = (alpha << 24) | (r << 16) | (g << 8) | b;
        return rgba(color);
    }

    static UiColor rgba(int color) {
        return new Impl(color);
    }

    static UiColor rgb(int r, int g, int b) {
        return rgba(r, g, b, 1.0f);
    }

    static UiColor rgb(int color) {
        return rgba(color | 0xFF000000);
    }

    static UiColor of(Color color) {
        if (color == null) return null;
        return rgba(color.getRGB());
    }
}
