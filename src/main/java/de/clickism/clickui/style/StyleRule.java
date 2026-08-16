package de.clickism.clickui.style;

import java.util.function.Predicate;

public record StyleRule(
    Predicate<StyleContext> condition,
    StyleConfig config
) {
}
