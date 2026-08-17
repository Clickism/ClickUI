package de.clickism.clickui;

import de.clickism.clickui.event.HitTester;
import de.clickism.clickui.event.events.MouseClickEvent;
import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.render.RenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class UiScreen extends Screen implements UiBuilder {

    private final Element<?> root;
    private final HitTester hitTester = new HitTester();
    private Element<?> hoveredElement = null;

    public UiScreen(Component component) {
        super(component);
        this.root = build();
    }

    public UiScreen() {
        this(Component.empty());
    }

    public static UiScreen create(Element<?> content) {
        return new UiScreen() {
            @Override
            public Element<?> build() {
                return content;
            }
        };
    }

    public abstract Element<?> build();

    @Override
    protected void init() {
        var screen = box()
            .width(this.width)
            .height(this.height);
        screen.children(this.root);
        new LayoutEngine().layout(screen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        // Update element states first
        updateState(mouseX, mouseY);
        // Then render the tree
        root.renderTree(new RenderContext(guiGraphics, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        updateState(x, y);
        if (hoveredElement == null) return false;

        // Fire mouse click event to the hovered element
        var event = new MouseClickEvent(x, y, button);
        hoveredElement.events().fireEvent(event);
        return true;
    }

    private void updateState(int mouseX, int mouseY) {
        var hit = hitTester.hitTest(root, mouseX, mouseY);
        if (hit == null) {
            // Clear hovered state if no element is hit
            if (hoveredElement != null) {
                hoveredElement.state().hovered(false);
                hoveredElement = null;
            }
            return;
        }

        // Update hovered state
        if (hoveredElement != null && hoveredElement != hit.target()) {
            hoveredElement.state().hovered(false);
        }
        hoveredElement = hit.target();
        hoveredElement.state().hovered(true);
    }
}
