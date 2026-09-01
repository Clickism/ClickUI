package de.clickism.clickui.style;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Represents a style that can be applied to UI elements, with support for conditional styles based on the context.
 */
// TODO: Custom renderer hooks
public class Style implements StyleBuilder<Style> {
    private final StyleMap styleMap = new StyleMap();
    private final List<ConditionalStyle> conditionalStyles = new ArrayList<>();

    /**
     * Creates a new empty style instance.
     */
    protected Style() {
        // Use static method to create an empty Style instance
    }

    /**
     * Creates an empty Style instance.
     *
     * @return An empty Style instance.
     */
    public static Style empty() {
        return new Style();
    }

    /**
     * Sets a style property to the given value.
     *
     * @param property The style property to set.
     * @param value    The value to set the style property to.
     * @param <T>      The type of the style property.
     * @return This Style instance, allowing for method chaining.
     */
    public <T> Style set(StyleProperty<T> property, T value) {
        styleMap.set(property, value);
        return this;
    }

    /**
     * Retrieves the value of a style property.
     *
     * @param property The style property to retrieve the value for.
     * @param <T>      The type of the style property.
     * @return The value of the style property, or null if it has not been set.
     */
    public <T> T get(StyleProperty<T> property) {
        return styleMap.get(property);
    }

    /**
     * Updates the value of a style property using the provided updater function.
     *
     * @param property The style property to update.
     * @param updater  The function to apply to the current value of the style property.
     * @param <T>      The type of the style property.
     * @return This Style instance, allowing for method chaining.
     */
    public <T> Style update(StyleProperty<T> property, UnaryOperator<T> updater) {
        T currentValue = styleMap.get(property);
        T newValue = updater.apply(currentValue);
        styleMap.set(property, newValue);
        return this;
    }

    /**
     * Adds a conditional style that will be applied when the given condition is met.
     *
     * @param condition A predicate that determines when the conditional style should be applied.
     * @param style     The style to apply when the condition is met.
     * @return This Style instance, allowing for method chaining.
     */
    public Style when(Predicate<StyleContext> condition, Style style) {
        conditionalStyles.add(new ConditionalStyle(condition, style));
        return this;
    }

    /**
     * Adds a conditional style that will be applied when the element is hovered.
     *
     * @param style The style to apply when the element is hovered.
     * @return This Style instance, allowing for method chaining.
     */
    public Style whenHovered(Style style) {
        return when(StyleContext::hovered, style);
    }

    /**
     * Adds a conditional style that will be applied when the element is focused.
     *
     * @param style The style to apply when the element is focused.
     * @return This Style instance, allowing for method chaining.
     */
    public Style whenFocused(Style style) {
        return when(StyleContext::focused, style);
    }

    /**
     * Adds a conditional style that will be applied when the element is disabled.
     *
     * @param style The style to apply when the element is disabled.
     * @return This Style instance, allowing for method chaining.
     */
    public Style whenDisabled(Style style) {
        return when(StyleContext::disabled, style);
    }

    /**
     * Resolves the style for the given context, merging the base style with any applicable conditional styles.
     *
     * @param context The context to resolve the style against.
     * @return A StyleMap containing the resolved style properties.
     */
    public StyleMap resolve(StyleContext context) {
        // Start with the screen style map
        StyleMap resolved = new StyleMap();
        resolved.merge(styleMap);

        // Apply conditional styles
        for (var conditionalStyle : conditionalStyles) {
            if (!conditionalStyle.matches(context)) {
                continue;
            }
            // Merge the conditional style into the resolved style
            var childStyle = conditionalStyle.style();
            var childResolved = childStyle.resolve(context);
            resolved.merge(childResolved);
        }

        return resolved;
    }

    /**
     * Merges another Style instance into this one, combining their style properties and conditional styles.
     *
     * @param other The other Style instance to merge into this one.
     * @return This Style instance, allowing for method chaining.
     */
    public Style merge(Style other) {
        this.styleMap.merge(other.styleMap);
        this.conditionalStyles.addAll(other.conditionalStyles);
        return this;
    }

    @Override
    public Style style() {
        return this;
    }
}
