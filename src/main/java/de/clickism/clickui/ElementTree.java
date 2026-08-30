package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.layout.Size;
import de.clickism.clickui.util.Util;

/**
 * Represents a tree of UI elements, with a root element and methods to prepare the tree for rendering.
 */
public class ElementTree {
    private static final LayoutEngine LAYOUT_ENGINE = new LayoutEngine();

    private final Element<?> root;

    /**
     * Creates a new ElementTree with the specified root element.
     *
     * @param root the root element of the tree
     */
    public ElementTree(Element<?> root) {
        this.root = root;
    }

    /**
     * Returns the root element of the element tree.
     *
     * @return the root element
     */
    public Element<?> root() {
        return root;
    }

    /**
     * Prepares the element tree for rendering by performing a rebuild of components and laying out the elements.
     *
     * @param screenSize the size of the screen to lay out the elements within
     */
    public void prepareRender(Size screenSize) {
        // First pass: rebuild components
        Util.preOrder(root, element -> {
            if (!(element instanceof Component<?> component)) return;
            // Rebuild component if needed
            component.performRebuildIfNeeded();
        });

        // Second pass: layout elements
        if (root.dirtyLayout()) {
            // Create a screen element wrapper
            var screen = new Box()
                .width(screenSize.width())
                .height(screenSize.height());
            screen.children(this.root);
            // Layout the tree
            LAYOUT_ENGINE.layout(screen);
            // Clear dirty state
            root.clearDirtyLayout();
        }
    }
}
