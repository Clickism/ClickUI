package de.clickism.clickui.layout;

public record Padding(int top, int right, int bottom, int left) {
    public static Padding ZERO = new Padding(0, 0, 0, 0);

    public static Padding uniform(int padding) {
        return new Padding(padding, padding, padding, padding);
    }
}
