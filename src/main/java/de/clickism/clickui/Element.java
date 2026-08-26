package de.clickism.clickui;

import de.clickism.clickui.event.Event;
import de.clickism.clickui.event.EventManager;
import de.clickism.clickui.event.EventTarget;
import de.clickism.clickui.layout.*;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.render.StyleRenderer;
import de.clickism.clickui.state.ElementState;
import de.clickism.clickui.state.ElementStateHolder;
import de.clickism.clickui.style.ResolvedStyle;
import de.clickism.clickui.style.Style;
import de.clickism.clickui.style.StyleContext;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * An element in the UI hierarchy.
 */
public abstract class Element<S extends Element<S>>
    implements Layoutable<S>, ElementStateHolder<S>, EventTarget<S>, UiBuilder {
    // TODO: Visibility, style, hover, events, etc.
    // TODO: Simple scheduler
    // TODO: Tooltip support
    /**
     * The parent element of this element, or null if this element is the root element.
     */
    private @Nullable Element<?> parent;
    /**
     * The children of this element.
     */
    private final List<Element<?>> children = new ArrayList<>();

    /**
     * Whether this element is the root of the tree and needs to be layed out again.
     */
    private boolean dirtyRoot = false;

    /**
     * Layout information for this element,
     */
    private final Layout layout = new Layout();
    /**
     * Style information for this element.
     */
    private Style style = new Style();
    /**
     * The state of this element, used for rendering.
     */
    private final ElementState state = new ElementState(this);
    /**
     * The event manager for this element, used for handling events.
     */
    private final EventManager events = new EventManager();

    private @Nullable Component tooltip = null;
    private int tooltipDelay = 500;

    /**
     * Calculated bounds of the element.
     */
    private Rect bounds = Rect.ZERO;

    /**
     * Whether this element can be hit by a mouse click event.
     */
    private boolean hitTestable = true;

    /**
     * Returns the intrinsic size of this element,
     * which is the size that this element would like to be if it could be any size.
     * <p>
     * Should not take children into account, as they will be layed out separately.
     *
     * @return The intrinsic size of this element.
     */
    public Size intrinsicSize() {
        return Size.ZERO;
    }

    /**
     * Returns the default minimum size of this element, which is the smallest size that this element can be
     * without breaking its layout or appearance.
     * <p>
     * This can be overriden via {@link Layout#minWidth(int)} and {@link Layout#minHeight(int)}.
     *
     * @return The minimum size of this element.
     */
    public Size defaultMinSize() {
        return Size.ZERO;
    }

    /**
     * Returns the effective minimum size of this element, which is the smallest size that this element can be
     * without breaking its layout or appearance, taking into account any overrides set.
     *
     * @return The effective minimum size of this element.
     */
    @ApiStatus.Internal
    public final Size effectiveMinSize() {
        var minWidth = width().min() != null
            ? width().min()
            : defaultMinSize().width();
        var minHeight = height().min() != null
            ? height().min()
            : defaultMinSize().height();
        return new Size(minWidth, minHeight);
    }

    /**
     * Returns the effective maximum size of this element, which is the largest size that this element can be
     * without breaking its layout or appearance, taking into account any overrides set.
     *
     * @return The effective maximum size of this element.
     */
    @ApiStatus.Internal
    public final Size effectiveMaxSize() {
        var maxWidth = width().max() != null
            ? width().max()
            : Integer.MAX_VALUE;
        var maxHeight = height().max() != null
            ? height().max()
            : Integer.MAX_VALUE;
        return new Size(maxWidth, maxHeight);
    }

    /**
     * Returns whether this element should shrink its children if they are overflowing the bounds of this element.
     *
     * @param axis the axis to check for overflow
     * @return Whether this element should shrink its children.
     */
    public boolean shrinkChildrenIfOverflowing(Axis axis) {
        return true;
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
     * Returns the parent of this element, or null if this element is the root element.
     *
     * @return the parent of this element, or null if this element is the root element
     */
    public @Nullable Element<?> parent() {
        return this.parent;
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
     * Returns a list of the children of this element that are layoutable,
     * i.e. that have a positioning of {@link Positioning#layout()}.
     *
     * @return a list of the children of this element that are layoutable
     */
    public List<Element<?>> layoutChildren() {
        return this.children.stream()
            .filter(child -> child.positioning().isLayout())
            .toList();
    }

    /**
     * Adds the given children to this element, and sets their parent to this element.
     *
     * @param children the children to add
     * @return this element
     */
    public S children(@Nullable Element<?>... children) {
        for (var child : children) {
            this.add(child);
        }
        return self();
    }

    /**
     * Adds the given children to this element, and sets their parent to this element.
     *
     * @param children the children to add
     * @return this element
     */
    public S children(Collection<Element<?>> children) {
        if (children == null) return self();
        for (var child : children) {
            this.add(child);
        }
        return self();
    }

    /**
     * Adds the given child to this element, and sets its parent to this element.
     *
     * @param child the child to add
     * @return this element
     */
    public S add(@Nullable Element<?> child) {
        // Allow null children to be passed in, but ignore them
        if (child == null) return self();
        this.children.add(child);
        if (child.parent != null) {
            child.parent.children.remove(child);
        }
        child.parent = this;
        return self();
    }

    /**
     * Removes the given child from this element, and sets its parent to null.
     *
     * @param child the child to remove
     * @return this element
     */
    public S remove(@Nullable Element<?> child) {
        if (child == null) return self();
        this.children.remove(child);
        if (child.parent == this) {
            child.parent = null;
        }
        return self();
    }

    /**
     * Removes all children from this element, and sets their parent to null.
     *
     * @return this element
     */
    public S clear() {
        for (var child : children) {
            child.parent = null;
        }
        this.children.clear();
        return self();
    }

    @Override
    public Layout layout() {
        return this.layout;
    }

    @Override
    public ElementState state() {
        return this.state;
    }

    /**
     * Returns the style of this element, which is used for rendering.
     *
     * @return the style of this element
     */
    public Style style() {
        return this.style;
    }

    /**
     * Returns the resolved style of this element.
     *
     * @return the resolved style of this element
     */
    public ResolvedStyle resolvedStyle() {
        return this.style.resolve(new StyleContext(this, this.state));
    }

    /**
     * Overrides the style of this element with the given style, replacing any existing style.
     *
     * @param style the style to override with
     * @return this element
     */
    public S overrideStyle(Style style) {
        this.style = style;
        return self();
    }

    @Override
    public EventManager events() {
        return this.events;
    }

    /**
     * Propagates the given event to this element and all of its children recursively.
     *
     * @param event the event to propagate
     */
    public void propagateEvent(Event event) {
        // Fire children first
        for (var child : children) {
            child.propagateEvent(event);
            if (event.state().consumed()) {
                return;
            }
        }

        // Fire this element's event manager
        this.events.fireEvent(event);
    }

    // TODO: Make style api nicer

    /**
     * Applies the given style consumer to this element's style, allowing for fluent style configuration.
     *
     * @param styleConsumer the style consumer to apply to this element's style
     * @return this element
     */
    public S style(Consumer<Style> styleConsumer) {
        styleConsumer.accept(this.style);
        return self();
    }

    /**
     * Invalidates the layout of this element tree.
     */
    public void invalidate() {
        root().dirtyRoot = true;
    }

    /**
     * Returns the root element of this element tree, which is the topmost ancestor of this element.
     *
     * @return the root element of this element tree
     */
    public Element<?> root() {
        Element<?> root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    /**
     * Returns whether this element is dirty and needs to be layed out again.
     *
     * @return whether this element is dirty and needs to be layed out again
     */
    @ApiStatus.Internal
    public boolean isDirty() {
        return root().dirtyRoot;
    }

    /**
     * Clears the dirty flag of this element.
     */
    @ApiStatus.Internal
    public void clearDirty() {
        root().dirtyRoot = false;
    }

    /**
     * Converts a point from the coordinate space of this element's parent
     * to the coordinate space of this element.
     * <p>
     * By default, this method returns the point unchanged, but subclasses
     * can override it to apply transformations such as translation, scaling, or rotation.
     *
     * @param point the point in the parent coordinate space
     * @return the point in this element's coordinate space
     */
    public Point toChildCoordinates(Point point) {
        return point;
    }

    /**
     * Sets the tooltip of this element, which is a text that is displayed when the user hovers over this element.
     *
     * @param tooltip the tooltip to set, or null to clear the tooltip
     * @return this element
     */
    public S tooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
        return self();
    }

    /**
     * Sets the tooltip of this element, which is a text that is displayed when the user hovers over this element.
     *
     * @param tooltip the tooltip to set, or null to clear the tooltip
     * @return this element
     */
    public S tooltip(@Nullable String tooltip) {
        this.tooltip = tooltip != null
            ? Component.literal(tooltip)
            : null;
        return self();
    }

    /**
     * Sets the delay in milliseconds before the tooltip is shown when the user hovers over this element.
     *
     * @param delay the delay in milliseconds
     * @return this element
     */
    public S tooltipDelay(int delay) {
        this.tooltipDelay = delay;
        return self();
    }

    /**
     * Returns whether this element can be hit by a mouse click event.
     *
     * @return whether this element can be hit by a mouse click event
     */
    public boolean hitTestable() {
        return this.hitTestable;
    }

    /**
     * Sets whether this element can be hit by a mouse click event.
     *
     * @param hitTestable whether this element can be hit by a mouse click event
     * @return this element
     */
    public S hitTestable(boolean hitTestable) {
        this.hitTestable = hitTestable;
        return self();
    }

    /**
     * Sets the given ref to this element, allowing external code to hold a reference to this element.
     *
     * @param ref the ref to set
     * @return this element
     */

    public S ref(Ref<S> ref) {
        ref.set(self());
        return self();
    }

    /**
     * Returns the total gap between all children of this element.
     *
     * @return the total gap between all children of this element
     */
    public int totalChildGap() {
        return childGap() * Math.max(0, layoutChildren().size() - 1);
    }

    /**
     * Renders this element and all of its children recursively.
     *
     * @param context the render context to render to
     */
    public void renderTree(RenderContext context) {
        this.renderWithStyle(context);
        // Render children
        for (var child : children) {
            child.renderTree(context);
        }
    }

    /**
     * Renders this element with its style applied, but does not render its children.
     *
     * @param context the render context to render with
     */
    public void renderWithStyle(RenderContext context) {
        new StyleRenderer(this, context).renderElement();
        // Render debug information if debug mode is enabled
        if (context.debug()) {
            renderDebugInfo(context);
        }
    }

    /**
     * Renders debug information for this element, such as its bounds and layout information.
     *
     * @param context the render context to render with
     */
    public void renderDebugInfo(RenderContext context) {
        // Render the bounds of this element as a red outline
        context.graphics().renderOutline(
            bounds().x(),
            bounds().y(),
            bounds().width(),
            bounds().height(),
            0xffff0000
        );

        // Render overlay if hovered
        if (state().hovered()) {
            context.graphics().fill(
                bounds().x(),
                bounds().y(),
                bounds().x() + bounds().width(),
                bounds().y() + bounds().height(),
                0x40ff0000
            );
        }
    }

    /**
     * Renders this element only, without rendering its children.
     *
     * @param context the render context to render with
     */
    public abstract void render(RenderContext context);

    /**
     * Initializes this element.
     * This method is guaranteed to be called once after the element
     * is added to the UI tree, and before it is rendered for the first time.
     */
    public void initialize() {
        // Nothing here
    }

    /**
     * This method should be called every tick to update the state of this element.
     * By default, this method does nothing, but subclasses can override it to perform
     * periodic updates, such as animations or state changes.
     */
    public void tick() {
        // Nothing here
    }
}
