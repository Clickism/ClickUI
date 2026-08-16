package de.clickism.clickui.layout;

public enum LayoutAxis {
    HORIZONTAL,
    VERTICAL;

    public LayoutAxis opposite() {
        return isHorizontal()
               ? VERTICAL
               : HORIZONTAL;
    }

    public boolean isHorizontal() {
        return this == HORIZONTAL;
    }
}
