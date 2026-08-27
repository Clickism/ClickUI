package de.clickism.clickui.render;

import de.clickism.clickui.Element;
import de.clickism.clickui.style.BorderPosition;
import de.clickism.clickui.style.StyleContext;

public class StyleRenderer {
    private final Element<?> element;
    private final RenderContext context;

    public StyleRenderer(Element<?> element, RenderContext context) {
        this.element = element;
        this.context = context;
    }

    public void renderElement() {
        var style = element.style().resolve(new StyleContext(element, element.state()));

        // Apply alpha
        context.graphics().setColor(1.0f, 1.0f, 1.0f, style.alpha());

        // Render background
        var background = style.background();
        if (background != null) {
            renderBackground(background.color());
        }

        // Render element itself
        element.render(context);

        // Render border
        var border = style.border();
        if (border != null && style.borderWidth() > 0) {
            renderBorder(border.color(), style.borderWidth(), style.borderPosition());
        }

        // Revert alpha
        context.graphics().setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    protected void renderBackground(int color) {
        context.graphics().fill(
            element.bounds().x(),
            element.bounds().y(),
            element.bounds().x() + element.bounds().width(),
            element.bounds().y() + element.bounds().height(),
            color
        );
    }

    protected void renderBorder(int color, int width, BorderPosition position) {
        int offset = switch (position) {
            case OUTSIDE -> -width;
            case CENTER -> -width / 2;
            case INSIDE -> 0;
        };
        while (width-- > 0) {
            context.graphics().renderOutline(
                element.bounds().x() + offset,
                element.bounds().y() + offset,
                element.bounds().width() - offset * 2,
                element.bounds().height() - offset * 2,
                color
            );
            offset++;
        }
    }
}
