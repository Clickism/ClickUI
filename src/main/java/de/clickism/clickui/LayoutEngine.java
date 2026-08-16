package de.clickism.clickui;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * The LayoutEngine class is responsible for laying out the elements in a UI hierarchy based on their sizing and
 * layout axis. It calculates the sizes and positions of each element and its children, taking into account padding,
 * child gaps, and sizing types (fixed, fit, or grow).
 */
public class LayoutEngine {
    /**
     * Returns a function that retrieves the axis size of an element based on the given layout axis.
     *
     * @param axis the layout axis to determine the axis size
     * @return a function that retrieves the axis size of an element
     */
    private static ToIntFunction<Element<?>> axisGetter(LayoutAxis axis) {
        return axis.isHorizontal()
               ? child -> child.bounds().width()
               : child -> child.bounds().height();
    }

    /**
     * Calculates the total gap between the children of the given element based on its child gap and number of children.
     *
     * @param element the element to calculate the total child gap for
     * @return the total gap between the children of the element
     */
    private static int totalChildGap(Element<?> element) {
        return element.childGap() * Math.max(0, element.children().size() - 1);
    }

    /**
     * Lays out the given root element and its children based on their sizing and layout axis.
     *
     * @param root the root element to layout
     */
    public void layout(Element<?> root) {
        // Set the root element size to the given size
        root.bounds(root.bounds().withPosition(0, 0));
        // Measure the fit size
        measureSizes(root);
        // Grow elements with GROW sizing
        growAll(root);
        // Calculate positions
        calculatePositions(root, 0, 0);
    }

    /**
     * Measures the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void measureSizes(Element<?> element) {
        // First measure the fit size
        Util.postOrder(element, this::measureElementSize);
    }

    /**
     * Measures the size of a single element based on its children and layout axis.
     *
     * @param element the element to measure
     */
    private void measureElementSize(Element<?> element) {
        // Fit the element size to its children and layout axis
        Size intrinsic = element.intrinsicSize();
        // Use intrinsic size of the element and add padding to it
        int width = intrinsic.width();
        int height = intrinsic.height();

        for (Element<?> child : element.children()) {
            // Use calculated size of children
            var bounds = child.bounds();
            int childWidth = bounds.width();
            int childHeight = bounds.height();

            // Add child size to parent size based on layout axis
            if (element.axis().isHorizontal()) {
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
        if (element.axis().isHorizontal()) {
            width += totalChildGap(element);
        } else {
            height += totalChildGap(element);
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

    /**
     * Grows the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to grow
     */
    private void growAll(Element<?> element) {
        Util.preOrder(element, this::growChildElements);
    }

    /**
     * Grows the child elements of the given element based on their sizing and layout axis.
     *
     * @param parent the element whose children will be grown
     */
    private void growChildElements(Element<?> parent) {
        int remainingAxis = calculateRemainingAxisSpace(parent);
        int remainingCross = calculateRemainingCrossSpace(parent);

        // Distribute remaining space to children with GROW sizing
        distributeAxisSpace(parent, remainingAxis);
        distributeCrossSpace(parent, remainingCross);
    }

    /**
     * Distributes the remaining axis space to the children of the given element that have a GROW sizing in the axis direction.
     *
     * @param parent        the parent element whose children will receive the remaining axis space
     * @param remainingAxis the remaining axis space to distribute
     */
    private void distributeAxisSpace(Element<?> parent, int remainingAxis) {
        boolean horizontal = parent.axis().isHorizontal();
        // Distribute remaining axis space to children
        Predicate<Element<?>> mapper = horizontal
                                       ? child -> child.width().type() == Sizing.Type.GROW
                                       : child -> child.height().type() == Sizing.Type.GROW;
        var growAxisChildren = parent.children()
            .stream().filter(mapper).toList();

        if (growAxisChildren.isEmpty() || remainingAxis <= 0) return;

        while (remainingAxis > 0) {
            // Find smallest axis size
            int smallest = growAxisChildren.stream()
                .mapToInt(axisGetter(parent.axis()))
                .min()
                .orElseThrow(); // List not empty, should not throw
            // Find second-smallest axis size
            var axisGetter = axisGetter(parent.axis());
            Integer secondSmallest = growAxisChildren.stream().map(axisGetter::applyAsInt)
                .filter(axis -> axis > smallest).reduce(Math::min).orElse(null);

            int axisToAdd;
            if (secondSmallest == null) {
                // All children same size, distribute remaining space evenly
                axisToAdd = Math.max(1, remainingAxis / growAxisChildren.size());
            } else {
                // Grow smallest one's size to match second smallest
                axisToAdd = secondSmallest - smallest;
                int equalShare = remainingAxis / growAxisChildren.size();
                equalShare = Math.max(1, equalShare); // At least add 1
                axisToAdd = Math.min(axisToAdd, equalShare);
            }
            // Don't add more than remaining space
            axisToAdd = Math.min(remainingAxis, axisToAdd);

            for (var child : growAxisChildren) {
                int axis = axisGetter.applyAsInt(child);
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
                if (remainingAxis <= 0) {
                    break;
                }
            }
        }
    }

    /**
     * Distributes the remaining cross space to the children of the given element that have a GROW sizing in the cross axis.
     *
     * @param parent         the parent element whose children will receive the remaining cross space
     * @param remainingCross the remaining cross space to distribute
     */
    private void distributeCrossSpace(Element<?> parent, int remainingCross) {
        // Distribute remaining cross space to children
        boolean horizontal = parent.axis().isHorizontal();
        for (var child : parent.children()) {
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
    private int calculateRemainingAxisSpace(Element<?> element) {
        boolean horizontal = element.axis().isHorizontal();
        int remainingAxis = horizontal
                            ? element.bounds().width()
                            : element.bounds().height();
        remainingAxis -= horizontal
                         ? element.padding().left() + element.padding().right()
                         : element.padding().top() + element.padding().bottom();
        remainingAxis -= totalChildGap(element);
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
    private int calculateRemainingCrossSpace(Element<?> element) {
        boolean horizontal = element.axis().isHorizontal();
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
    private void calculatePositions(Element<?> element, int x, int y) {
        if (element.positioning().type() == Positioning.Type.ABSOLUTE) {
            // Absolute positioning, use the specified position
            x = element.positioning().x();
            y = element.positioning().y();
        }
        if (element.positioning().type() == Positioning.Type.RELATIVE) {
            // Relative positioning, offset from the current position
            x += element.positioning().x();
            y += element.positioning().y();
        }
        // Set the position of the element
        element.bounds(element.bounds().withPosition(x, y));

        // Calculate position of children based on layout axis and padding
        var horizontal = element.axis().isHorizontal();

        var padding = element.padding();
        int currentX = x + padding.left();
        int currentY = y + padding.top();

        // Adjust the starting position based on the main alignment
        int mainOffset = mainOffsetToAlign(element);
        if (horizontal) {
            currentX += mainOffset;
        } else {
            currentY += mainOffset;
        }

        // Calcualte available cross space for alignment
        int availableCross = availableCrossSpace(element);

        for (var child : element.children()) {
            // Calculate cross offset based on alignment
            int crossOffset = crossOffsetToAlign(element, child, availableCross);
            // Position the child based on the current position and layout axis and alignment
            if (horizontal) {
                calculatePositions(
                    child,
                    currentX,
                    currentY + crossOffset
                );
                currentX += child.bounds().width() + element.childGap();
            } else {
                calculatePositions(
                    child,
                    currentX + crossOffset,
                    currentY
                );
                currentY += child.bounds().height() + element.childGap();
            }
        }
    }

    /**
     * Calculates the offset needed to align the children of the given element based on its main alignment.
     *
     * @param element the element to calculate the offset for
     * @return the offset needed to align the children of the element
     */
    private int mainOffsetToAlign(Element<?> element) {
        // Total size occupied by children in the layout axis, including gaps
        int childrenSize = element.children().stream()
            .mapToInt(axisGetter(element.axis()))
            .sum();
        childrenSize += totalChildGap(element);

        var horizontal = element.axis().isHorizontal();
        var padding = element.padding();
        int available = horizontal
                        ? element.bounds().width() - padding.left() - padding.right()
                        : element.bounds().height() - padding.top() - padding.bottom();

        int remaining = available - childrenSize;

        return switch (element.mainAlign()) {
            case START -> 0;
            case CENTER -> remaining / 2;
            case END -> remaining;
        };
    }

    /**
     * Calculates the available cross-axis space for the given element, taking into account its padding.
     *
     * @param element the element to calculate the available cross-axis space for
     * @return the available cross-axis space for the element
     */
    private int availableCrossSpace(Element<?> element) {
        var horizontal = element.axis().isHorizontal();
        var padding = element.padding();
        return horizontal
               ? element.bounds().height() - padding.top() - padding.bottom()
               : element.bounds().width() - padding.left() - padding.right();
    }

    /**
     * Calculates the offset needed to align a child element within its parent element
     * based on the parent's cross alignment and the available cross-axis space.
     *
     * @param element        the parent element
     * @param child          the child element to align
     * @param availableCross the available cross-axis space for the parent element
     * @return the offset needed to align the child element within the parent element
     */
    private int crossOffsetToAlign(Element<?> element, Element<?> child, int availableCross) {
        int childSize = element.axis().isHorizontal()
                        ? child.bounds().height()
                        : child.bounds().width();

        int remaining = availableCross - childSize;

        return switch (element.crossAlign()) {
            case START -> 0;
            case CENTER -> remaining / 2;
            case END -> remaining;
        };
    }
}
