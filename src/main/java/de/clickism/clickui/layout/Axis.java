package de.clickism.clickui.layout;

public enum Axis {
    HORIZONTAL,
    VERTICAL;

    public Axis opposite() {
        return isHorizontal()
               ? VERTICAL
               : HORIZONTAL;
    }

    public boolean isHorizontal() {
        return this == HORIZONTAL;
    }
}
