package de.clickism.clickui;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An element in the UI hierarchy.
 */
public abstract class Element {

    // UI Tree

    /**
     * The children of this element.
     */
    private final List<Element> children = new ArrayList<>();
    /**
     * The parent element of this element, or null if this element is the root element.
     */
    private @Nullable Element parent;

    // Layout
    private LayoutAxis layoutAxis = LayoutAxis.VERTICAL;

    private Sizing width = Sizing.fit();
    private Sizing height = Sizing.fit();

    private Padding padding = Padding.ZERO;
    private int childGap = 0;

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

    public Rect bounds() {
        return bounds;
    }

    void bounds(Rect bounds) {
        this.bounds = bounds;
    }

    public Element vertical() {
        this.layoutAxis = LayoutAxis.VERTICAL;
        return this;
    }

    public Element horizontal() {
        this.layoutAxis = LayoutAxis.HORIZONTAL;
        return this;
    }

    public Element axis(LayoutAxis axis) {
        this.layoutAxis = axis;
        return this;
    }

    public LayoutAxis axis() {
        return this.layoutAxis;
    }

    public Element childGap(int gap) {
        this.childGap = gap;
        return this;
    }

    public int childGap() {
        return this.childGap;
    }

    public Element padding(int padding) {
        this.padding = Padding.uniform(padding);
        return this;
    }

    public Element padding(Padding padding) {
        this.padding = padding;
        return this;
    }

    public Padding padding() {
        return this.padding;
    }

    public Element width(Sizing sizing) {
        this.width = sizing;
        return this;
    }

    public Element width(int width) {
        this.width = Sizing.fixed(width);
        return this;
    }

    public Sizing width() {
        return this.width;
    }

    public Element height(Sizing sizing) {
        this.height = sizing;
        return this;
    }

    public Element height(int height) {
        this.height = Sizing.fixed(height);
        return this;
    }

    public Sizing height() {
        return this.height;
    }

    public Size size() {
        return new Size(this.width().value(), this.height().value());
    }

    public Element children(Element... children) {
        for (Element child : children) {
            this.children.add(child);
            if (child.parent != null) {
                child.parent.children.remove(child);
            }
            child.parent = this;
        }
        return this;
    }

    public List<Element> children() {
        return Collections.unmodifiableList(this.children);
    }

    public @Nullable Element parent() {
        return this.parent;
    }

    /**
     * Renders this element and all of its children recursively.
     *
     * @param context the render context to render to
     */
    public void renderTree(RenderContext context) {
        this.render(context);
        // Render children
        for (Element child : children) {
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
