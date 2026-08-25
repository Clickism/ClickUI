package de.clickism.clickui.elements.input;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A UI element that allows users to input numbers.
 * It supports optional decimal and negative number input.
 */
public class NumberField extends Field<NumberField> {

    private boolean allowDecimal = true;
    private boolean allowNegative = true;

    private double maxValue = Double.MAX_VALUE;
    private double minValue = Double.MIN_VALUE;

    /**
     * Creates a new NumberField instance.
     */
    public NumberField() {
        super();
        // Filter only numbers
        this.filterInput(string -> {
            var allowed = "0-9";
            if (allowNegative) {
                allowed += "-";
            }
            if (allowDecimal) {
                allowed += ".,";
            }
            return string.replaceAll("[^" + allowed + "]", "");
        });
        // Set validator to check if the input is a valid number
        this.validator(this::isValid);
    }

    /**
     * Checks if the given string is a valid number according to the current settings.
     *
     * @param string the string to validate
     * @return true if the string is a valid number, false otherwise
     */
    private boolean isValid(String string) {
        if (string.isEmpty()) {
            return true;
        }
        var value = parseValue();
        if (value == null) {
            return false;
        }
        // Check if between bounds
        return value >= minValue && value <= maxValue;
    }

    /**
     * Sets whether decimal numbers are allowed in the input.
     *
     * @param allowDecimal whether to allow decimal numbers
     * @return the current instance
     */
    public NumberField allowDecimal(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
        return this;
    }

    /**
     * Sets whether negative numbers are allowed in the input.
     *
     * @param allowNegative whether to allow negative numbers
     * @return the current instance
     */
    public NumberField allowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
        return this;
    }

    /**
     * Sets the minimum value allowed for the input.
     *
     * @param minValue the minimum value
     * @return the current instance
     */
    public NumberField minValue(double minValue) {
        this.minValue = minValue;
        return this;
    }

    /**
     * Sets the maximum value allowed for the input.
     *
     * @param maxValue the maximum value
     * @return the current instance
     */
    public NumberField maxValue(double maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    /**
     * Parses the current value of the input field as a Double.
     *
     * @return parsed value or null if invalid
     */
    private @Nullable Double parseValue() {
        try {
            var fixed = this.value().replace(',', '.');
            var parsed = Double.parseDouble(fixed);
            if (Double.isNaN(parsed) || Double.isInfinite(parsed)) {
                return null;
            }
            return Mth.clamp(parsed, minValue, maxValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Gets the current value of the input field as a double.
     *
     * @return the current value as a double, or 0.0 if the value is invalid
     */
    public double doubleValue() {
        var value = parseValue();
        if (value == null) {
            return 0.0;
        }
        return value;
    }

    /**
     * Gets the current value of the input field as an Optional Double.
     *
     * @return an optional containing the current value as a Double
     */
    public Optional<Double> optionalDoubleValue() {
        return Optional.ofNullable(parseValue());
    }

    /**
     * Gets the current value of the input field as an int.
     *
     * @return the current value as an int, or 0 if the value is invalid
     */
    public int intValue() {
        return (int) this.doubleValue();
    }

    /**
     * Gets the current value of the input field as an Optional Integer.
     *
     * @return an optional containing the current value as an Integer
     */
    public Optional<Integer> optionalIntValue() {
        return optionalDoubleValue().map(Double::intValue);
    }

    /**
     * Gets the current value of the input field as a float.
     *
     * @return the current value as a float, or 0.0f if the value is invalid
     */
    public float floatValue() {
        return (float) this.doubleValue();
    }

    /**
     * Gets the current value of the input field as an Optional Float.
     *
     * @return an optional containing the current value as a Float
     */
    public Optional<Float> optionalFloatValue() {
        return optionalDoubleValue().map(Double::floatValue);
    }
}
