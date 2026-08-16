package de.clickism.clickui.style;

@FunctionalInterface
public interface StyleConfig {
    /**
     * Configures the style for the given resolved style.
     */
    void configure(ResolvedStyle style);
}
