package de.clickism.clickui;

import java.util.List;

public class LayoutEngine {
    /**
     * Lays out the given root element and its children based on their sizing and layout axis.
     *
     * @param root the root element to layout
     * @param size the size to lay out the root element within
     */
    public void layout(Element root, Size size) {
        // Set the root element size to the given size
        root.bounds(new Rect(0, 0, size.width(), size.height()));
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
        int gapCount = Math.max(0, element.children().size() - 1);
        if (element.axis() == LayoutAxis.HORIZONTAL) {
            width += element.childGap() * gapCount;
        } else {
            height += element.childGap() * gapCount;
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

    private void growChildElements(Element element) {
//        if (element.axis() == LayoutAxis.HORIZONTAL) {
//            int remainingWidth = element.bounds().width();
//            remainingWidth -= element.padding().left() + element.padding().right();
//            remainingWidth -= element.childGap() * Math.max(0, element.children().size() - 1);
//            remainingWidth -= element.children().stream().mapToInt(child -> child.bounds().width()).sum();
//
//            int remainingHeight = element.bounds().height();
//            remainingHeight -= element.padding().top() + element.padding().bottom();
//
//            // Distribute remaining width to children with grow sizing
//            var growChildren = element.children().stream()
//                    .filter(child -> child.width().type() == Sizing.Type.GROW)
//                    .toList();
//            if (growChildren.isEmpty()) return;
//            while (remainingWidth > 0) {
//                int smallest = growChildren.get(0).bounds().width();
//                int secondSmallest = Integer.MAX_VALUE;
//                int widthToAdd = remainingWidth;
//                for (Element child : growChildren) {
//                    int width = child.bounds().width();
//                    if (width < smallest) {
//                        secondSmallest = smallest;
//                        smallest = width;
//                    }
//                    if (width > smallest) {
//                        secondSmallest = Math.min(secondSmallest, width);
//                        widthToAdd = secondSmallest - smallest;
//                    }
//                }
//
//                widthToAdd = Math.min(widthToAdd, remainingWidth / growChildren.size());
//
//                for (Element child : growChildren) {
//                    if (child.bounds().width() == smallest) {
//                        int newWidth = child.bounds().width() + widthToAdd;
//                        child.bounds(child.bounds().withWidth(newWidth));
//                        remainingWidth -= widthToAdd;
//                    }
//                }
//            }
//
//            // Distribute remaining height to children with grow sizing
//            for (Element child : element.children()) {
//                if (child.height().type() == Sizing.Type.GROW) {
//                    child.bounds(child.bounds().withSize(child.bounds().width(), remainingHeight));
//                }
//            }
//        } else {
//            int remainingHeight = element.bounds().height();
//            remainingHeight -= element.padding().top() + element.padding().bottom();
//            remainingHeight -= element.childGap() * Math.max(0, element.children().size() - 1);
//            remainingHeight -= element.children().stream().mapToInt(child -> child.bounds().height()).sum();
//
//            int remainingWidth = element.bounds().width();
//            remainingWidth -= element.padding().left() + element.padding().right();
//
//            // Distribute remaining height to children with grow sizing
//            for (Element child : element.children()) {
//                if (child.height().type() == Sizing.Type.GROW) {
//                    int height = child.bounds().height() + remainingHeight;
//                    child.bounds(child.bounds().withSize(child.bounds().width(), height));
//                }
//                if (child.width().type() == Sizing.Type.GROW) {
//                    child.bounds(child.bounds().withSize(remainingWidth, child.bounds().height()));
//                }
//            }
//        }
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
