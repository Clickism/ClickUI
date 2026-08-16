package de.clickism.clickui;

import de.clickism.clickui.layout.Layout;
import de.clickism.clickui.layout.Layoutable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An element in the UI hierarchy.
 */
public abstract class Element<S extends Element<S>> implements Layoutable<S> {
    // TODO: Visibility, style, hover, events, etc.
    /**
     * The children of this element.
     */
    private final List<Element<?>> children = new ArrayList<>();
    /**
     * Layout information for this element,
     */
    private final Layout layout = new Layout();
    /**
     * The parent element of this element, or null if this element is the root element.
     */
    private @Nullable Element<?> parent;
    /**
     * Calculated bounds of the element.
     */
    private Rect bounds = Rect.ZERO;

    /**
     * Returns the intrinsic size of this element, which is the size that this element would like to be if it could be any size.
     *
     * @return The intrinsic size of this element.
     */
    public Size intrinsicSize() {
        return Size.ZERO;
    }

    /**
     * Returns the minimum size of this element, which is the smallest size that this element can be without breaking its layout.
     *
     * @return The minimum size of this element.
     */
    public Size minSize() {
        return Size.ZERO;
    }

    /**
     * Gets the bounds of this element, which is the rectangle that this element occupies in the coordinate space.
     *
     * @return The bounds of this element.
     */
    public Rect bounds() {
        return bounds;
    }

    /**
     * Sets the bounds of this element, which is the rectangle that this element occupies in the coordinate space.
     * This method is intended to be called by the layout system, and should not be called directly by user code.
     *
     * @param bounds The new bounds of this element.
     */
    @ApiStatus.Internal
    public void bounds(Rect bounds) {
        this.bounds = bounds;
    }

    /**
     * Adds the given children to this element, and sets their parent to this element.
     *
     * @param children the children to add
     * @return this element
     */
    public S children(Element<?>... children) {
        for (var child : children) {
            this.children.add(child);
            if (child.parent != null) {
                child.parent.children.remove(child);
            }
            child.parent = this;
        }
        return self();
    }

    /**
     * Returns an unmodifiable list of the children of this element.
     *
     * @return an unmodifiable list of the children of this element
     */
    public List<Element<?>> children() {
        return Collections.unmodifiableList(this.children);
    }

    /**
     * Returns the parent of this element, or null if this element is the root element.
     *
     * @return the parent of this element, or null if this element is the root element
     */
    public @Nullable Element<?> parent() {
        return this.parent;
    }

    @Override
    public Layout layout() {
        return this.layout;
    }

    /**
     * Invalidates the layout of this element and all of its children.
     */
    public void invalidate() {
        // TODO: Implement
    }

    /**
     * Renders this element and all of its children recursively.
     *
     * @param context the render context to render to
     */
    public void renderTree(RenderContext context) {
        this.render(context);
        // TODO: Remove debug rendering
        context.graphics().renderOutline(this.bounds().x(), this.bounds().y(), this.bounds().width(), this.bounds().height(), 0xffff0000);
        // Render children
        for (var child : children) {
            child.renderTree(context);
        }
    }

    /**
     * Renders this element only, without rendering its children.
     *
     * @param context the render context to render with
     */
    public abstract void render(RenderContext context);
}
