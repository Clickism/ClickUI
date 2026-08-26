package de.clickism.clickui.event;

import de.clickism.clickui.Element;
import de.clickism.clickui.elements.Button;
import de.clickism.clickui.layout.Point;
import org.jetbrains.annotations.Nullable;

/**
 * A utility class for performing hit testing on UI elements.
 */
public class HitTester {

    @Nullable
    public HitTestResult hitTest(Element<?> element, int mouseX, int mouseY) {
        return hitTest(element, new Point(mouseX, mouseY));
    }

    @Nullable
    private HitTestResult hitTest(Element<?> element, Point mouse) {
        var bounds = element.bounds();

        // Search children in reverse order
        var children = element.children();
        for (int i = children.size() - 1; i >= 0; i--) {
            var child = children.get(i);
            var childMouse = element.toChildCoordinates(mouse);
            var result = hitTest(child, childMouse);

            if (result != null) {
                return result;
            }
        }

        if (!bounds.contains(mouse)) return null;

        // If no child was hit, take this element as the target.
        if (element.hitTestable()) {
            return new HitTestResult(element, mouse.x(), mouse.y());
        }
        return null;
    }
}
