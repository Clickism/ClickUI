package de.clickism.clickui.layout;

import de.clickism.clickui.Element;
import de.clickism.clickui.Wrappable;
import de.clickism.clickui.util.Util;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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
        width += padding.horizontal();
        height += padding.vertical();

        // Add child gap
        if (element.axis().isHorizontal()) {
            width += element.totalChildGap();
        } else {
            height += element.totalChildGap();
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
                    growOrShrinkMainChildren(parent);
                }
                if (measureHeight) {
                    growOrShrinkCrossChildren(parent);
                }
            } else {
                if (measureHeight) {
                    growOrShrinkMainChildren(parent);
                }
                if (measureWidth) {
                    growOrShrinkCrossChildren(parent);
                }
            }
        });
    }

    /**
     * Distributes the remaining main space to the children of the given element
     * that have a GROW sizing in the axis direction.
     *
     * @param parent the parent element whose children will receive the remaining main space
     */
    private void growOrShrinkMainChildren(Element<?> parent) {
        var axis = parent.axis();

        // Calculate remaining axis space
        int remainingMain = parent.bounds().mainSize(axis);
        remainingMain -= parent.padding().mainPadding(axis);
        remainingMain -= parent.totalChildGap();
        remainingMain -= totalChildrenMainSize(parent);

        if (remainingMain > 0) {
            growMainChildren(parent, remainingMain);
        } else if (remainingMain < 0) {
            shrinkMainChildren(parent, remainingMain);
        }
    }

    /**
     * Grows the sizes of the children of the given element based on their sizing and layout axis.
     *
     * @param parent         the parent element whose children will be grown
     * @param remainingSpace the remaining main space to distribute to the children
     */
    private void growMainChildren(Element<?> parent, int remainingSpace) {
        var axis = parent.axis();
        var horizontal = axis.isHorizontal();
        // Distribute remaining axis space to children
        var growChildren = getGrowableChildren(parent);
        // Grow elements
        while (remainingSpace > 0 && !growChildren.isEmpty()) {
            var extremes = ListExtremes.ofElements(growChildren, axis);
            var smallest = extremes.smallest();
            var secondSmallest = extremes.secondSmallest();

            int axisToAdd;
            if (secondSmallest == null) {
                // All children same size, distribute remaining space evenly
                axisToAdd = Math.max(1, remainingSpace / growChildren.size());
            } else {
                // Grow smallest one's size to match second smallest
                axisToAdd = secondSmallest - smallest;
                int equalShare = remainingSpace / growChildren.size();
                equalShare = Math.max(1, equalShare); // At least add 1
                axisToAdd = Math.min(axisToAdd, equalShare);
            }
            // Don't add more than remaining space
            axisToAdd = Math.min(remainingSpace, axisToAdd);

            var atMaxSize = new ArrayList<Element<?>>();

            for (var child : growChildren) {
                int main = child.bounds().mainSize(axis);
                if (main != smallest) continue;
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
                int added = child.bounds().mainSize(axis) - main;
                remainingSpace -= added;
                if (remainingSpace <= 0) {
                    break;
                }
            }

            // Remove children that can't be grown anymore
            growChildren.removeAll(atMaxSize);
        }
    }

    /**
     * Returns a mutable list of the children of the given parent element
     * that can grow in the axis direction based on their sizing.
     *
     * @param parent the parent element whose children will be checked
     */
    private List<Element<?>> getGrowableChildren(Element<?> parent) {
        return parent.children()
            .stream()
            .filter(child -> child.mainSizing().isGrow())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Shrinks the sizes of the given element and its children based on their sizing and layout axis.
     *
     * @param parent         the parent element whose children will be shrunk
     * @param remainingSpace the remaining main space to shrink
     */
    private void shrinkMainChildren(Element<?> parent, int remainingSpace) {
        var axis = parent.axis();
        if (!parent.shrinkChildrenIfOverflowing(axis)) {
            // Don't shrink children if the parent doesn't allow it
            return;
        }

        var shrinkChildren = getShrinkableChildren(parent);
        // Shrink elements
        while (remainingSpace < 0 && !shrinkChildren.isEmpty()) {
            var extremes = ListExtremes.ofElements(shrinkChildren, axis);
            var largest = extremes.largest();
            var secondLargest = extremes.secondLargest();

            int axisToRemove;
            if (secondLargest == null) {
                // All children same size, distribute remaining space evenly
                axisToRemove = Math.max(1, -remainingSpace / shrinkChildren.size());
            } else {
                // Shrink largest one's size to match second largest
                axisToRemove = largest - secondLargest;
                int equalShare = -remainingSpace / shrinkChildren.size();
                equalShare = Math.max(1, equalShare); // At least remove 1
                axisToRemove = Math.min(axisToRemove, equalShare);
            }
            // Don't remove more than remaining space
            axisToRemove = Math.min(-remainingSpace, axisToRemove);

            var atMinSize = new ArrayList<Element<?>>();
            for (var child : shrinkChildren) {
                int oldMain = child.bounds().mainSize(axis);
                if (oldMain != largest) continue;
                // Shrink largest boxes by width to remove
                if (axis.isHorizontal()) {
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
                int removed = oldMain - child.bounds().mainSize(axis);
                remainingSpace += removed;
                if (remainingSpace >= 0) {
                    break;
                }
            }

            // Remove children that can't be shrunk anymore
            shrinkChildren.removeAll(atMinSize);
        }
    }

    /**
     * Returns a mutable list of the children of the given parent element
     * that can shrink in the axis direction based on their sizing.
     *
     * @param parent the parent element whose children will be checked
     */
    private List<Element<?>> getShrinkableChildren(Element<?> parent) {
        var axis = parent.axis();
        return parent.children()
            .stream()
            .filter(child -> child.effectiveMinSize().mainSize(axis) < child.bounds().mainSize(axis))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Distributes the remaining cross space to the children of the given element
     * that have a GROW sizing in the cross axis.
     *
     * @param parent the parent element whose children will receive the remaining cross space
     */
    private void growOrShrinkCrossChildren(Element<?> parent) {
        var axis = parent.axis();

        // Calculate remaining cross space
        int totalCross = parent.bounds().crossSize(axis) - parent.padding().crossPadding(axis);

        // Grow or shrink all children
        for (var child : parent.children()) {
            var max = child.effectiveMaxSize();
            var min = child.effectiveMinSize();

            int cross = child.bounds().crossSize(axis);

            boolean canGrow = child.crossSizing().isGrow();
            boolean canShrink = child.effectiveMinSize().crossSize(axis) < child.bounds().crossSize(axis);

            if (cross < totalCross && !canGrow) {
                // Can't grow this child, skip it
                continue;
            }

            if (cross > totalCross && !canShrink) {
                // Can't shrink this child, skip it
                continue;
            }

            // Set to the total cross size, clamped to the child's min and max sizes
            if (axis.isHorizontal()) {
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
        int childrenSize = totalChildrenMainSize(element);
        childrenSize += element.totalChildGap();

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

    /**
     * A record to hold the smallest, second smallest, largest, and second-largest values in a list of integers.
     *
     * @param smallest       the smallest value in the list
     * @param secondSmallest the second-smallest value in the list, or null if there is no second smallest value
     * @param largest        the largest value in the list
     * @param secondLargest  the second-largest value in the list, or null if there is no second largest value
     */
    private record ListExtremes(
        Integer smallest,
        @Nullable Integer secondSmallest,
        Integer largest,
        @Nullable Integer secondLargest
    ) {
        /**
         * Calculates the list extremes of the given list of elements based on their sizes
         * in the specified layout axis.
         *
         * @param elements the list of elements
         * @param axis     the layout axis to consider for size comparison
         * @return list extremes
         * @throws IllegalArgumentException if the list of elements is empty
         */
        static ListExtremes ofElements(List<Element<?>> elements, LayoutAxis axis) {
            var sizes = elements.stream()
                .mapToInt(element -> element.bounds().mainSize(axis))
                .boxed()
                .toList();
            return of(sizes);
        }

        /**
         * Calculates the list extremes of the given list of integers.
         *
         * @param list the list of integers
         * @return list extremes
         * @throws IllegalArgumentException if the list is empty
         */
        static ListExtremes of(List<Integer> list) {
            if (list.isEmpty()) {
                throw new IllegalArgumentException("List must not be empty");
            }
            int smallest = Integer.MAX_VALUE;
            Integer secondSmallest = null;
            int largest = Integer.MIN_VALUE;
            Integer secondLargest = null;

            for (int value : list) {
                if (value < smallest) {
                    secondSmallest = smallest == Integer.MAX_VALUE
                        ? null
                        : smallest;
                    smallest = value;
                } else if (value > smallest && (secondSmallest == null || value < secondSmallest)) {
                    secondSmallest = value;
                }

                if (value > largest) {
                    secondLargest = largest == Integer.MIN_VALUE
                        ? null
                        : largest;
                    largest = value;
                } else if (value < largest && (secondLargest == null || value > secondLargest)) {
                    secondLargest = value;
                }
            }

            return new ListExtremes(smallest, secondSmallest, largest, secondLargest);
        }
    }

    /**
     * Calculates the total main size of the children of the given parent element
     *
     * @param parent the parent element
     * @return the total main size of the children
     */
    private static int totalChildrenMainSize(Element<?> parent) {
        var axis = parent.axis();
        return parent.children().stream()
            .mapToInt(child -> child.bounds().mainSize(axis))
            .sum();
    }
}
