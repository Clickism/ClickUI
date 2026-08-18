package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Point;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.util.Util;

// TODO: Overflow hidden, show, scroll etc.
/**
 * A simple container element that can hold other elements
 * and provides scrolling functionality in case of
 * vertical overflow.
 */
public class Box extends Element<Box> {
    private static final double DEFAULT_SCROLL_RATE = 4.5;

    /**
     * The current vertical scroll offset of the box.
     */
    private double scrollY;
    /**
     * The scroll rate.
     */
    private double scrollRate = DEFAULT_SCROLL_RATE;
    /**
     * Whether the box is scrollable or not.
     */
    private boolean scrollable = true;

    /**
     * Creates a new Box element with default settings.
     */
    public Box() {
        this.onScroll(event -> {
            this.scrollY -= event.delta() * scrollRate;
            // Clamp scrollY to be within the valid range
            if (this.scrollY < 0) {
                this.scrollY = 0;
            } else if (this.scrollY > maxScrollY()) {
                this.scrollY = maxScrollY();
            }
        });
    }

    /**
     * Sets whether this box is scrollable or not,
     * in case the content overflows vertically.
     *
     * @param scrollable true to make the box scrollable, false to disable scrolling
     * @return this box instance for chaining
     */
    public Box scrollable(boolean scrollable) {
        this.scrollable = scrollable;
        return this;
    }

    /**
     * Sets the scroll rate for this box.
     * The scroll rate determines how fast the content scrolls in response to scroll events.
     * <p>
     * Default is 1.0 corresponding to the default scroll rate.
     *
     * @param multiplier the scroll rate multiplier
     * @return this box instance for chaining
     */
    public Box scrollRate(double multiplier) {
        this.scrollRate = multiplier * DEFAULT_SCROLL_RATE;
        return this;
    }

    private double maxScrollY() {
        return Math.max(0, contentHeight() - bounds().height());
    }

    /**
     * The total height of the content inside this box, including the gaps between children.
     *
     * @return the total content height
     */
    private int contentHeight() {
        int totalPadding = padding().top() + padding().bottom();
        int totalGap = Util.totalChildGap(this);
        int childrenHeight = this.children().stream()
            .mapToInt(child -> child.bounds().height())
            .sum();
        return childrenHeight + totalGap + totalPadding;
    }

    /**
     * Whether the content of this box is overflowing vertically and requires scrolling.
     *
     * @return true if the content is overflowing, false otherwise
     */
    private boolean isOverflowing() {
        return contentHeight() > bounds().height();
    }

    @Override
    public Point toChildCoordinates(Point point) {
        return new Point(point.x(), (int) (point.y() + scrollY));
    }

    @Override
    public void renderTree(RenderContext context) {
        if (!scrollable) {
            // Skip scroll rendering
            super.renderTree(context);
            return;
        }
        // TODO: Render scroll bar if overflowing
        // Render self
        this.renderWithStyle(context);
        // Render children with scroll offset
        var graphics = context.graphics();
        // Enable scissor to clip children
        var bounds = bounds();
        var x1 = bounds.x();
        var y1 = bounds.y();
        var x2 = bounds.x() + bounds.width();
        var y2 = bounds.y() + bounds.height();
        graphics.enableScissor(x1, y1, x2, y2);

        graphics.pose().pushPose();
        // Apply scroll offset
        graphics.pose().translate(0, -scrollY, 0);

        // Render children
        for (var child : children()) {
            child.renderTree(context);
        }

        graphics.pose().popPose();
        // Disable scissor
        graphics.disableScissor();
    }

    @Override
    public void render(RenderContext context) {
        // Nothing to render for the box itself
    }
}
