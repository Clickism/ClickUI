package de.clickism.clickui;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class LayoutEngine {
    /**
     * Lays out the given root element and its children based on their sizing and layout axis.
     *
     * @param root the root element to layout
     */
    public void layout(Element root) {
        // Set the root element size to the given size
        root.bounds(root.bounds().withPosition(0, 0));
        // Measure the fit size
        measureSizes(root);
        // Measure the grow size
        growAll(root);
        // Calculate positions
        calculatePositions(root, 0, 0);
    }

    /**
     * Measures the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void measureSizes(Element element) {
        // First measure the fit size
        Util.postOrder(element, this::measureElementSize);
    }

    /**
     * Measures the size of a single element based on its children and layout axis.
     *
     * @param element the element to measure
     */
    private void measureElementSize(Element element) {
        // Fit the element size to its children and layout axis
        Size intrinsic = element.intrinsicSize();
        // Use intrinsic size of the element and add padding to it
        int width = intrinsic.width();
        int height = intrinsic.height();

        for (Element child : element.children()) {
            // Use calculated size of children
            var bounds = child.bounds();
            int childWidth = bounds.width();
            int childHeight = bounds.height();

            // Add child size to parent size based on layout axis
            if (element.axis() == LayoutAxis.HORIZONTAL) {
                width += childWidth;
                height = Math.max(height, childHeight);
            } else {
                width = Math.max(width, childWidth);
                height += childHeight;
            }
        }

        // Add padding to the calculated size
        var padding = element.padding();
        width += padding.left() + padding.right();
        height += padding.top() + padding.bottom();

        // Add child gap
        if (element.axis() == LayoutAxis.HORIZONTAL) {
            width += element.totalChildGap();
        } else {
            height += element.totalChildGap();
        }

        // Check if the element has fixed sizing and override the calculated size if so
        if (element.width().type() == Sizing.Type.FIXED) {
            width = element.width().value();
        }
        if (element.height().type() == Sizing.Type.FIXED) {
            height = element.height().value();
        }

        // Set the measured size to the element
        element.bounds(element.bounds().withSize(width, height));
    }

    private void growAll(Element element) {
        Util.preOrder(element, this::growChildElements);
    }

    private ToIntFunction<Element> crossGetter(LayoutAxis axis) {
        return axis == LayoutAxis.HORIZONTAL
                ? child -> child.bounds().height()
                : child -> child.bounds().width();
    }

    private ToIntFunction<Element> axisGetter(LayoutAxis axis) {
        return axis == LayoutAxis.HORIZONTAL
                ? child -> child.bounds().width()
                : child -> child.bounds().height();
    }

    private void growChildElements(Element element) {
        int remainingAxis = calculateRemainingAxisSpace(element);
        int remainingCross = calculateRemainingCrossSpace(element);

        distributeAxisSpace(element, remainingAxis);
        distributeCrossSpace(element, remainingCross);
    }

    private void distributeAxisSpace(Element element, int remainingAxis) {
        boolean horizontal = element.axis() == LayoutAxis.HORIZONTAL;
        // Distribute remaining axis space to children
        Predicate<Element> mapper = horizontal
                ? child -> child.width().type() == Sizing.Type.GROW
                : child -> child.height().type() == Sizing.Type.GROW;
        var growAxisChildren = element.children().stream().filter(mapper).toList();

        if (growAxisChildren.isEmpty()) return;

        while (remainingAxis > 0) {
            // Find smallest axis size
            int smallest = growAxisChildren.stream()
                    .mapToInt(axisGetter(element.axis()))
                    .min()
                    .orElse(Integer.MAX_VALUE);
            // Find second smallest axis size
            int secondSmallest = growAxisChildren.stream()
                    .mapToInt(axisGetter(element.axis()))
                    .filter(axis -> axis > smallest)
                    .min()
                    .orElse(Integer.MAX_VALUE);

            // By default, make the smallest one's size match the second smallest
            int axisToAdd = secondSmallest - smallest;
            // Make sure we at most add as much as remaining space
            axisToAdd = Math.min(remainingAxis, axisToAdd);
            // At each step, add only the max share possible
            axisToAdd = Math.min(axisToAdd, remainingAxis / growAxisChildren.size());

            for (Element child : growAxisChildren) {
                int axis = axisGetter(element.axis()).applyAsInt(child);
                if (axis != smallest) continue;
                // Grow smallest boxes by width to add
                if (horizontal) {
                    int width = child.bounds().width() + axisToAdd;
                    child.bounds(child.bounds().withWidth(width));
                } else {
                    int height = child.bounds().height() + axisToAdd;
                    child.bounds(child.bounds().withHeight(height));
                }
                remainingAxis -= axisToAdd;
            }
        }
    }

    private void distributeCrossSpace(Element element, int remainingCross) {
        // Distribute remaining cross space to children
        boolean horizontal = element.axis() == LayoutAxis.HORIZONTAL;
        for (Element child : element.children()) {
            boolean growCross = horizontal
                    ? child.height().type() == Sizing.Type.GROW
                    : child.width().type() == Sizing.Type.GROW;
            if (!growCross) continue;
            // Grow to remaining cross if smaller
            if (horizontal) {
                int height = Math.max(child.bounds().height(), remainingCross);
                child.bounds(child.bounds().withHeight(height));
            } else {
                int width = Math.max(child.bounds().width(), remainingCross);
                child.bounds(child.bounds().withWidth(width));
            }
        }
    }

    /**
     * Calculates the remaining soace in the axis of the element
     *
     * @param element the element to use
     * @return remaining space in axis
     */
    private int calculateRemainingAxisSpace(Element element) {
        boolean horizontal = element.axis() == LayoutAxis.HORIZONTAL;
        int remainingAxis = horizontal
                ? element.bounds().width()
                : element.bounds().height();
        remainingAxis -= horizontal
                ? element.padding().left() + element.padding().right()
                : element.padding().top() + element.padding().bottom();
        remainingAxis -= element.totalChildGap();
        remainingAxis -= element.children().stream()
                .mapToInt(axisGetter(element.axis()))
                .sum();
        return remainingAxis;
    }

    /**
     * Calculates th remaining cross space in the element
     *
     * @param element the elment to use
     * @return remaining cross space
     */
    private int calculateRemainingCrossSpace(Element element) {
        boolean horizontal = element.axis() == LayoutAxis.HORIZONTAL;
        int remainingHeight = horizontal
                ? element.bounds().height()
                : element.bounds().width();
        remainingHeight -= horizontal
                ? element.padding().top() + element.padding().bottom()
                : element.padding().left() + element.padding().right();
        return remainingHeight;
    }

    /**
     * Calculates the positions of the given element and its children based on their layout axis and padding.
     *
     * @param element the element to calculate positions for
     * @param x       the x position to start laying out the element
     * @param y       the y position to start laying out the element
     */
    private void calculatePositions(Element element, int x, int y) {
        element.bounds(element.bounds().withPosition(x, y));

        int currentX = x + element.padding().left();
        int currentY = y + element.padding().top();

        for (Element child : element.children()) {
            // Position the child based on the current position and layout axis
            if (element.axis() == LayoutAxis.HORIZONTAL) {
                calculatePositions(child, currentX, currentY);
                currentX += child.bounds().width() + element.childGap();
            } else {
                calculatePositions(child, currentX, currentY);
                currentY += child.bounds().height() + element.childGap();
            }
        }
    }
}
