package de.clickism.clickui.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of style properties and their corresponding values.
 */
public record StyleData(
    Map<StyleProperty<?>, Object> properties,
    List<RenderHook> renderHooks
) {
    /**
     * Creates a new empty StyleData.
     */
    public StyleData() {
        this(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Sets the value of the specified style property.
     *
     * @param property the style property to set
     * @param value    the value to set for the style property
     * @param <T>      the type of the style property
     */
    public <T> void set(StyleProperty<T> property, T value) {
        properties.put(property, value);
    }

    /**
     * Returns the value of the specified style property, or its default value if not set.
     *
     * @param property the style property to retrieve
     * @param <T>      the type of the style property
     * @return the value of the style property, or its default value if not set
     */
    @SuppressWarnings("unchecked")
    public <T> T get(StyleProperty<T> property) {
        return (T) properties.getOrDefault(property, property.defaultValue());
    }

    /**
     * Returns the internal map of style properties and their values.
     *
     * @return the internal map of style properties and their values
     */
    @Override
    public Map<StyleProperty<?>, Object> properties() {
        return properties;
    }

    /**
     * Merges the properties of another StyleData into this one.
     * If a property exists in both maps, the value from the other map will overwrite the value in this map.
     *
     * @param other the other StyleData to merge into this one
     */
    public void merge(StyleData other) {
        properties.putAll(other.properties());
        renderHooks.addAll(other.renderHooks());
    }
}
