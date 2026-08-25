package de.clickism.clickui.style;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// TODO: Refactor styles to be inheritance based
public final class Style implements Styleable<Style> {
    private final ResolvedStyle baseStyle = new ResolvedStyle();
    private final List<StyleRule> rules = new ArrayList<>();

    @Override
    public ResolvedStyle style() {
        return baseStyle;
    }

    public Style whenHovered(StyleConfig config) {
        when(StyleContext::hovered, config);
        return this;
    }

    public Style whenFocused(StyleConfig config) {
        when(StyleContext::focused, config);
        return this;
    }

    public Style when(Predicate<StyleContext> condition, StyleConfig config) {
        rules.add(new StyleRule(condition, config));
        return this;
    }

    public ResolvedStyle resolve(StyleContext context) {
        ResolvedStyle resolvedStyle = baseStyle.copy();
        for (StyleRule rule : rules) {
            if (rule.condition().test(context)) {
                rule.config().configure(resolvedStyle);
            }
        }
        return resolvedStyle;
    }

    public static Style empty() {
        return new Style();
    }
}
