package de.clickism.clickui.layout;

import de.clickism.clickui.Element;
import de.clickism.clickui.Wrappable;
import de.clickism.clickui.util.Util;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The LayoutEngine class is responsible for laying out the elements in a UI hierarchy based on their sizing and
 * layout axis. It calculates the sizes and positions of each element and its children, taking into account padding,
 * child gaps, and sizing types (fixed, fit, or grow).
 */
public class LayoutEngine {
    /**
     * Lays out the given root element and its children based on their sizing and layout axis.
     *
     * @param root the root element to layout
     */
    // TODO: Respect max sizing when growing or shrinking
    public void layout(Element<?> root) {
        // Set the root element's position to (0, 0)
        root.bounds(root.bounds().withPosition(0, 0));
        // Measure intrinsic widths
        measureIntrinsicWidths(root);
        // Grow widths of elements with GROW sizing
        growOrShrinkWidths(root);
        // Wrap elements that implement Wrappable
        wrapElements(root);
        // Measure intrinsic heights
        measureIntrinsicHeights(root);
        // Grow heights of elements with GROW sizing
        growOrShrinkHeights(root);
        // Calculate positions
        calculatePositions(root, 0, 0);
    }

    /**
     * Measures the intrinsic sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void measureIntrinsicWidths(Element<?> element) {
        measureIntrinsicSize(element, true, false);
    }

    /**
     * Measures the intrinsic sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void measureIntrinsicHeights(Element<?> element) {
        measureIntrinsicSize(element, false, true);
    }

    /**
     * Recursively measures the sizes of the given element
     * and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void measureIntrinsicSize(Element<?> element, boolean measureWidth, boolean measureHeight) {
        // First measure the sizes of the children
        for (var child : element.children()) {
            measureIntrinsicSize(child, measureWidth, measureHeight);
        }

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
            width += Util.totalChildGap(element);
        } else {
            height += Util.totalChildGap(element);
        }

        // Clamp the calculated size to the element's min and max sizes
        // This also handles fixed sizes, as min and max sizes are equal for fixed sizing
        var min = element.effectiveMinSize();
        var max = element.effectiveMaxSize();
        width = Mth.clamp(width, min.width(), max.width());
        height = Mth.clamp(height, min.height(), max.height());

        // Set the measured size to the element
        if (measureWidth) {
            element.bounds(element.bounds().withWidth(width));
        }
        if (measureHeight) {
            element.bounds(element.bounds().withHeight(height));
        }
    }

    /**
     * Measures the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void growOrShrinkWidths(Element<?> element) {
        growOrShrinkSize(element, true, false);
    }

    /**
     * Measures the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to measure
     */
    private void growOrShrinkHeights(Element<?> element) {
        growOrShrinkSize(element, false, true);
    }

    /**
     * Grows the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param element the element to grow
     */
    private void growOrShrinkSize(Element<?> element, boolean measureWidth, boolean measureHeight) {
        Util.preOrder(element, parent -> {
            if (parent.axis().isHorizontal()) {
                if (measureWidth) {
                    growOrShrinkAxisChildren(parent);
                }
                if (measureHeight) {
                    growOrShrinkCrossChildren(parent);
                }
            } else {
                if (measureHeight) {
                    growOrShrinkAxisChildren(parent);
                }
                if (measureWidth) {
                    growOrShrinkCrossChildren(parent);
                }
            }
        });
    }

    /**
     * Distributes the remaining axis space to the children of the given element
     * that have a GROW sizing in the axis direction.
     *
     * @param parent the parent element whose children will receive the remaining axis space
     */
    private void growOrShrinkAxisChildren(Element<?> parent) {
        boolean horizontal = parent.axis().isHorizontal();

        // Calculate remaining axis space
        int remainingAxis = horizontal
            ? parent.bounds().width()
            : parent.bounds().height();
        remainingAxis -= horizontal
            ? parent.padding().left() + parent.padding().right()
            : parent.padding().top() + parent.padding().bottom();
        remainingAxis -= Util.totalChildGap(parent);
        remainingAxis -= parent.children().stream()
            .mapToInt(Util.axisGetter(parent.axis()))
            .sum();

        if (remainingAxis > 0) {
            // Distribute remaining axis space to children
            Predicate<Element<?>> mapper = horizontal
                ? child -> child.width().type() == Sizing.Type.GROW
                : child -> child.height().type() == Sizing.Type.GROW;
            var growChildren = parent.children()
                .stream()
                .filter(mapper)
                .collect(Collectors.toCollection(ArrayList::new));

            // Grow elements
            while (remainingAxis > 0 && !growChildren.isEmpty()) {
                // Find smallest axis size
                int smallest = growChildren.stream()
                    .mapToInt(Util.axisGetter(parent.axis()))
                    .min()
                    .orElseThrow(); // List not empty, should not throw
                // Find second-smallest axis size
                var axisGetter = Util.axisGetter(parent.axis());
                Integer secondSmallest = growChildren.stream()
                    .map(axisGetter::applyAsInt)
                    .filter(axis -> axis > smallest)
                    .reduce(Math::min)
                    .orElse(null);

                int axisToAdd;
                if (secondSmallest == null) {
                    // All children same size, distribute remaining space evenly
                    axisToAdd = Math.max(1, remainingAxis / growChildren.size());
                } else {
                    // Grow smallest one's size to match second smallest
                    axisToAdd = secondSmallest - smallest;
                    int equalShare = remainingAxis / growChildren.size();
                    equalShare = Math.max(1, equalShare); // At least add 1
                    axisToAdd = Math.min(axisToAdd, equalShare);
                }
                // Don't add more than remaining space
                axisToAdd = Math.min(remainingAxis, axisToAdd);

                var atMaxSize = new ArrayList<Element<?>>();

                for (var child : growChildren) {
                    int axis = axisGetter.applyAsInt(child);
                    if (axis != smallest) continue;
                    // Grow smallest boxes by width to add
                    var max = child.effectiveMaxSize();
                    if (horizontal) {
                        int width = child.bounds().width() + axisToAdd;
                        if (width > max.width()) {
                            width = max.width();
                            // Can't grow this child anymore
                            atMaxSize.add(child);
                        }
                        child.bounds(child.bounds().withWidth(width));
                    } else {
                        int height = child.bounds().height() + axisToAdd;
                        if (height > max.height()) {
                            height = max.height();
                            // Can't grow this child anymore
                            atMaxSize.add(child);
                        }
                        child.bounds(child.bounds().withHeight(height));
                    }
                    int added = axisGetter.applyAsInt(child) - axis;
                    remainingAxis -= added;
                    if (remainingAxis <= 0) {
                        break;
                    }
                }

                // Remove children that can't be grown anymore
                growChildren.removeAll(atMaxSize);
            }
        } else if (remainingAxis < 0) {
            if (!parent.shrinkChildrenIfOverflowing(horizontal)) {
                // Don't shrink children if the parent doesn't allow it
                return;
            }
            // Shrink all children that can be shrunk
            Predicate<Element<?>> mapper = horizontal
                ? child -> child.effectiveMinSize().width() < child.bounds().width()
                : child -> child.effectiveMinSize().height() < child.bounds().height();
            var shrinkChildren = parent.children().stream()
                .filter(mapper)
                .collect(Collectors.toCollection(ArrayList::new));

            // Shrink elements
            while (remainingAxis < 0 && !shrinkChildren.isEmpty()) {
                // Find largest axis size
                int largest = shrinkChildren.stream()
                    .mapToInt(Util.axisGetter(parent.axis()))
                    .max()
                    .orElseThrow(); // List not empty, should not throw
                // Find second-largest axis size
                var axisGetter = Util.axisGetter(parent.axis());
                Integer secondLargest = shrinkChildren.stream()
                    .map(axisGetter::applyAsInt)
                    .filter(axis -> axis < largest)
                    .reduce(Math::max)
                    .orElse(null);

                int axisToRemove;
                if (secondLargest == null) {
                    // All children same size, distribute remaining space evenly
                    axisToRemove = Math.max(1, -remainingAxis / shrinkChildren.size());
                } else {
                    // Shrink largest one's size to match second largest
                    axisToRemove = largest - secondLargest;
                    int equalShare = -remainingAxis / shrinkChildren.size();
                    equalShare = Math.max(1, equalShare); // At least remove 1
                    axisToRemove = Math.min(axisToRemove, equalShare);
                }
                // Don't remove more than remaining space
                axisToRemove = Math.min(-remainingAxis, axisToRemove);

                var atMinSize = new ArrayList<Element<?>>();
                for (var child : shrinkChildren) {
                    int oldAxis = axisGetter.applyAsInt(child);
                    if (oldAxis != largest) continue;
                    // Shrink largest boxes by width to remove
                    if (horizontal) {
                        int width = child.bounds().width() - axisToRemove;
                        if (width < child.effectiveMinSize().width()) {
                            width = child.effectiveMinSize().width();
                            // Can't shrink this child anymore
                            atMinSize.add(child);
                        }
                        child.bounds(child.bounds().withWidth(width));
                    } else {
                        int height = child.bounds().height() - axisToRemove;
                        if (height < child.effectiveMinSize().height()) {
                            height = child.effectiveMinSize().height();
                            // Can't shrink this child anymore
                            atMinSize.add(child);
                        }
                        child.bounds(child.bounds().withHeight(height));
                    }
                    int removed = oldAxis - axisGetter.applyAsInt(child);
                    remainingAxis += removed;
                    if (remainingAxis >= 0) {
                        break;
                    }
                }

                // Remove children that can't be shrunk anymore
                shrinkChildren.removeAll(atMinSize);
            }
        }
    }

    /**
     * Distributes the remaining cross space to the children of the given element
     * that have a GROW sizing in the cross axis.
     *
     * @param parent the parent element whose children will receive the remaining cross space
     */
    private void growOrShrinkCrossChildren(Element<?> parent) {
        boolean horizontal = parent.axis().isHorizontal();

        // Calculate remaining cross space
        int totalCross = horizontal
            ? parent.bounds().height() - parent.padding().vertical()
            : parent.bounds().width() - parent.padding().horizontal();

        // Grow or shrink all children
        for (var child : parent.children()) {
            var max = child.effectiveMaxSize();
            var min = child.effectiveMinSize();

            int cross = horizontal
                ? child.bounds().height()
                : child.bounds().width();

            boolean canGrow = horizontal
                ? child.height().type() == Sizing.Type.GROW
                : child.width().type() == Sizing.Type.GROW;
            boolean canShrink = horizontal
                ? child.effectiveMinSize().height() < child.bounds().height()
                : child.effectiveMinSize().width() < child.bounds().width();

            if (cross < totalCross && !canGrow) {
                // Can't grow this child, skip it
                continue;
            }

            if (cross > totalCross && !canShrink) {
                // Can't shrink this child, skip it
                continue;
            }

            // Set to the total cross size, clamped to the child's min and max sizes
            if (horizontal) {
                int height = Mth.clamp(totalCross, min.height(), max.height());
                child.bounds(child.bounds().withHeight(height));
            } else {
                int width = Mth.clamp(totalCross, min.width(), max.width());
                child.bounds(child.bounds().withWidth(width));
            }
        }
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
            .mapToInt(Util.axisGetter(element.axis()))
            .sum();
        childrenSize += Util.totalChildGap(element);

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

    /**
     * Wraps the text of the given element if it implements the Wrappable interface
     * and if it's overflowing its bounds.
     * <p>
     * This method is called recursively for all child elements.
     *
     * @param element the element to wrap
     */
    private void wrapElements(Element<?> element) {
        if (element instanceof Wrappable wrappable) {
            int maxWidth = element.bounds().width()
                           - element.padding().horizontal();
            boolean overflowing = element.intrinsicSize().width() > maxWidth;
            if (maxWidth > 0 && overflowing) {
                wrappable.wrap(maxWidth);
            }
        }

        // Recursively wrap children
        for (var child : element.children()) {
            wrapElements(child);
        }
    }
}
