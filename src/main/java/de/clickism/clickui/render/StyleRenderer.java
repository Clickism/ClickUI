package de.clickism.clickui.render;

import de.clickism.clickui.Element;
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
            renderBackground(background.getRGB());
        }

        // Render element itself
        element.render(context);

        // Render border
        var border = style.border();
        if (border != null && style.borderWidth() > 0) {
            renderBorder(border.getRGB(), style.borderWidth());
        }
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

    protected void renderBorder(int color, int width) {
        while (width-- > 0) {
            context.graphics().renderOutline(
                element.bounds().x() - width - 1,
                element.bounds().y() - width - 1,
                element.bounds().width() + width * 2 + 1,
                element.bounds().height() + width * 2 + 1,
                color
            );
        }
    }
}
