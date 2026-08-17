package de.clickism.clickui.event;

import de.clickism.clickui.Element;

public record HitTestResult(
    Element<?> target,
    int x,
    int y
) {
}
