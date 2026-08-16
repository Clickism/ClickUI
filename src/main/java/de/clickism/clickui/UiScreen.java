package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class UiScreen extends Screen {

    private Element<?> built;

    public UiScreen(Component component) {
        super(component);
        this.built = build();
    }

    public UiScreen() {
        this(Component.empty());
    }

    public abstract Element<?> build();

    @Override
    protected void init() {
        var root = new Box()
                .width(this.width)
                .height(this.height);
        root.children(built);
        new LayoutEngine().layout(root);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        built.renderTree(new RenderContext(guiGraphics, mouseX, mouseY, delta));
    }

    public static UiScreen create(Element<?> content) {
        return new UiScreen() {
            @Override
            public Element<?> build() {
                return content;
            }
        };
    }
}
