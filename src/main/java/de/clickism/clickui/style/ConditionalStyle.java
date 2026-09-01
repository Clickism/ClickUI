package de.clickism.clickui.style;

import java.util.function.Predicate;

/**
 * Represents a conditional style that applies a specific style when a given condition is met.
 *
 * @param condition the predicate that determines when the style should be applied
 * @param style     the style to apply when the condition is met
 */
public record ConditionalStyle(
    Predicate<StyleContext> condition,
    Style style
) {
    /**
     * Checks if the given context matches the condition for this conditional style.
     *
     * @param context the context to check against the condition
     * @return true if the context matches the condition, false otherwise
     */
    public boolean matches(StyleContext context) {
        return condition.test(context);
    }
}
