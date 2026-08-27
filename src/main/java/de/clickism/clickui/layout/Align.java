package de.clickism.clickui.layout;

/**
 * Represents the alignment of an element within its parent container.
 */
public enum Align {
    /**
     * Aligns the element to the start of the axis.
     */
    START,
    /**
     * Aligns the element to the center of the axis.
     */
    CENTER,
    /**
     * Aligns the element to the end of the axis.
     */
    END;

    /**
     * Returns the factor corresponding to the alignment.
     * START corresponds to 0.0, CENTER to 0.5, and END to 1.0.
     *
     * @return the factor for the alignment
     */
    public float factor() {
        return switch (this) {
            case START -> 0f;
            case CENTER -> 0.5f;
            case END -> 1f;
        };
    }
}
