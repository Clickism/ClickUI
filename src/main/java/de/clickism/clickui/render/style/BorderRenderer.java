package de.clickism.clickui.render.style;

import de.clickism.clickui.layout.Rect;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.style.Border;
import de.clickism.clickui.style.FourSided;

import static de.clickism.clickui.style.FourSided.Side.*;

/**
 * A utility class responsible for rendering borders around a rectangular area based on the specified border properties.
 */
public class BorderRenderer {
    private final RenderContext context;

    private final Rect bounds;
    private final FourSided<Border> border;

    /**
     * Constructs a BorderRenderer with the specified RenderContext, bounds, and border properties.
     *
     * @param context the RenderContext used for rendering
     * @param bounds  the rectangular bounds of the UI element
     * @param border  the four sided border representing the border properties for each side
     */
    public BorderRenderer(RenderContext context, Rect bounds, FourSided<Border> border) {
        this.context = context;
        this.bounds = bounds;
        this.border = border;
    }

    /**
     * Renders the borders of the given border and bounds.
     */
    public void render() {
        var graphics = context.graphics();

        var top = border.top();
        var right = border.right();
        var bottom = border.bottom();
        var left = border.left();

        int x1 = bounds.x();
        int y1 = bounds.y();
        int x2 = bounds.x() + bounds.width();
        int y2 = bounds.y() + bounds.height();

        var topBand = band(y1, top, TOP);
        var rightBand = band(x2, right, RIGHT);
        var bottomBand = band(y2, bottom, BOTTOM);
        var leftBand = band(x1, left, LEFT);

        // Horizontal borders handle corners
        // If left visible, start from left border's start
        int xStart = left.isVisible()
            ? leftBand.start
            : x1;
        // If right visible, end at right border's end
        int xEnd = right.isVisible()
            ? rightBand.end
            : x2;

        // Render horizontal borders
        if (top.isVisible()) {
            graphics.fill(xStart, topBand.start, xEnd, topBand.end, top.color().color());
        }
        if (bottom.isVisible()) {
            graphics.fill(xStart, bottomBand.start, xEnd, bottomBand.end, bottom.color().color());
        }

        // Vertical borders render without corners
        if (left.isVisible()) {
            graphics.fill(leftBand.start, bounds.y(), leftBand.end, y2, left.color().color());
        }
        if (right.isVisible()) {
            graphics.fill(rightBand.start, bounds.y(), rightBand.end, y2, right.color().color());
        }
    }

    /**
     * Calculates the range of pixels that a border occupies based on its position and width.
     *
     * @param edge   the edge coordinate (x or y) where the border is applied
     * @param border the border to calculate the range for
     * @param side   the side of the border (top, right, bottom, left)
     * @return a Band representing the start and end coordinates of the border
     */
    private Band band(int edge, Border border, FourSided.Side side) {
        int start;
        int end;
        int width = border.width();
        boolean lowerSide = (side == TOP || side == LEFT);
        switch (border.position()) {
            case OUTSIDE -> {
                if (lowerSide) {
                    start = edge - width;
                    end = edge;
                } else {
                    start = edge;
                    end = edge + width;
                }
            }
            case INSIDE -> {
                if (lowerSide) {
                    start = edge;
                    end = edge + width;
                } else {
                    start = edge - width;
                    end = edge;
                }
            }
            case CENTER -> {
                int half = width / 2;
                if (lowerSide) {
                    start = edge - half;
                    end = edge + (width - half);
                } else {
                    start = edge - (width - half);
                    end = edge + half;
                }
            }
            default -> throw new IllegalArgumentException("Unknown border position: " + border.position());
        }
        return new Band(start, end);
    }

    /**
     * A simple record to represent a range of pixels (start and end) that a border occupies.
     *
     * @param start the starting pixel coordinate of the border
     * @param end   the ending pixel coordinate of the border
     */
    private record Band(int start, int end) {
    }
}
