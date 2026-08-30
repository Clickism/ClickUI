package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.render.TooltipRenderer;
import de.clickism.clickui.util.Util;

/**
 * Represents a tree of UI elements, with a root element and methods to prepare the tree for rendering.
 */
public class UiElementTree {
    private static final LayoutEngine LAYOUT_ENGINE = new LayoutEngine();

    private final UiElement<?> root;

    /**
     * Creates a new ElementTree with the specified root element.
     *
     * @param root the root element of the tree
     */
    public UiElementTree(UiElement<?> root) {
        this.root = root;
    }

    /**
     * Returns the root element of the element tree.
     *
     * @return the root element
     */
    public UiElement<?> root() {
        return root;
    }

    /**
     * Invalidates the layout of the element tree,
     * ensureing that it will be re-laid out during the next render pass.
     */
    public void invalidateLayout() {
        root.invalidateLayout();
    }

    /**
     * Prepares the element tree for rendering by performing a rebuild of components and laying out the elements.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public void prepareRender(int screenWidth, int screenHeight) {
        // First pass: rebuild components
        Util.preOrder(root, element -> {
            if (!(element instanceof UiComponent<?> component)) return;
            // Rebuild component if needed
            component.performRebuildIfNeeded();
        });

        // Second pass: layout elements
        if (root.dirtyLayout()) {
            // Create a screen element wrapper
            var screen = new Box()
                .width(screenWidth)
                .height(screenHeight);
            screen.children(this.root);
            // Layout the tree
            LAYOUT_ENGINE.layout(screen);
            // Clear dirty state
            root.clearDirtyLayout();
        }
    }

    /**
     * Renders the element tree using the provided render context.
     *
     * @param context the render context to use for rendering
     */
    public void render(RenderContext context) {
        // Render the tree
        // TODO: Add a way to modify render context
        root.renderTree(context);
    }

    /**
     * Renders tooltips for all elements in the tree that have visible tooltips.
     *
     * @param context the render context to use for rendering tooltips
     */
    public void renderTooltips(RenderContext context) {
        Util.preOrder(root, element -> {
            if (!element.isTooltipVisible()) return;
            new TooltipRenderer(element.tooltip(), context).render();
        });
    }

    /**
     * Ticks all elements in the tree.
     */
    public void tick() {
        Util.preOrder(root, UiElement::tick);
    }
}
