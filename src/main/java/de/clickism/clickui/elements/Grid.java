package de.clickism.clickui.elements;

import de.clickism.clickui.Element;
import de.clickism.clickui.layout.Axis;
import de.clickism.clickui.render.RenderContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple grid layout that arranges its children in a specified number of columns.
 * <p>
 * The axis can be changed to switch between horizontal and vertical layouts.
 */
public class Grid extends Element<Grid> {
    private int columns;

    private final List<Element<?>> gridChildren = new ArrayList<>();

    /**
     * Creates a new grid layout with the specified number of columns.
     *
     * @param columns the number of columns in the grid
     */
    public Grid(int columns) {
        this.columns = columns;
        this.rebuildGrid();
        this.invalidate();
    }

    /**
     * Rebuilds the grid layout based on the current number of columns and rows,
     * and children.
     */
    private void rebuildGrid() {
        clear();

        var boxes = new ArrayList<Element<?>>();

        var axis = this.axis();
        for (int i = 0; i < this.columns; i++) {
            boxes.add(box()
                .scrollable(false)
                .childGap(this.childGap())
                .axis(axis.isHorizontal()
                    ? Axis.VERTICAL
                    : Axis.HORIZONTAL));
        }

        for (int i = 0; i < gridChildren.size(); i++) {
            int index = i / this.columns;
            boxes.get(index).add(gridChildren.get(i));
        }

        axis(axis.isHorizontal()
            ? Axis.HORIZONTAL
            : Axis.VERTICAL);
        boxes.forEach(super::add);
    }

    /**
     * Sets the number of columns in the grid layout.
     *
     * @param columns the number of columns to set
     * @return this grid instance for method chaining
     */
    public Grid columns(int columns) {
        if (columns < 0) {
            throw new IllegalArgumentException("Columns cannot be negative");
        }
        this.columns = columns;
        this.rebuildGrid();
        this.invalidate();
        return this;
    }

    @Override
    public Grid add(@Nullable Element<?> child) {
        if (child == null) return this;
        gridChildren.add(child);
        rebuildGrid();
        return this;
    }

    @Override
    public Grid remove(@Nullable Element<?> child) {
        if (child == null) return this;
        gridChildren.remove(child);
        rebuildGrid();
        return this;
    }

    @Override
    public void render(RenderContext context) {
        // No extra rendering
    }
}
