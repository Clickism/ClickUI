package de.clickism.clickui;

import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * A screen that contains a UI tree and handles rendering and layout.
 * <p>
 * Subclasses should implement the {@link build()} method to define the structure of the UI.
 */
public abstract class UiScreen extends UiEventScreen implements UiBuilder {
    /**
     * The root element of the UI tree.
     */
    private final Element<?> root;

    /**
     * The parent screen of this UiScreen, if any.
     * This can be used to navigate back to the previous screen.
     */
    private @Nullable UiScreen parent;

    /**
     * Creates a new UiScreen with the specified title component.
     *
     * @param component the title component of the screen
     */
    public UiScreen(Component component) {
        super(component);
        this.root = build();
    }

    /**
     * Creates a new UiScreen.
     */
    public UiScreen() {
        this(Component.empty());
    }

    /**
     * Creates a new UiScreen with the specified content element.
     *
     * @param content the root element of the UI tree
     * @return a new UiScreen instance with the specified content
     */
    public static UiScreen create(Element<?> content) {
        return new UiScreen() {
            @Override
            public Element<?> build() {
                return content;
            }
        };
    }

    /**
     * Returns the parent screen of this UiScreen, if any.
     *
     * @return the parent screen, or null if there is no parent
     */
    public @Nullable UiScreen parent() {
        return parent;
    }

    /**
     * Sets the parent screen of this UiScreen.
     *
     * @param parent the parent screen to set, or null if there is no parent
     */
    public void parent(@Nullable UiScreen parent) {
        this.parent = parent;
    }

    /**
     * Opens the specified UiScreen, setting this screen as its parent.
     *
     * @param screen the UiScreen to open
     */
    public void open(UiScreen screen) {
        screen.parent(this);
        Util.openScreen(screen);
    }

    /**
     * Opens this UiScreen in the Minecraft client.
     */
    public void open() {
        Util.openScreen(this);
    }

    /**
     * Navigates back to the parent screen, if any.
     * If there is no parent screen, it closes the current screen.
     */
    public void back() {
        if (parent == null) {
            Util.openScreen(null);
            return;
        }
        Util.openScreen(parent);
    }

    @Override
    protected Element<?> eventRoot() {
        return root; // Return the root element for event handling
    }

    /**
     * Builds the root element of the UI tree.
     * This method should be implemented by subclasses to define the structure of the UI.
     * <p>
     * This function is called only once during the initialization of the screen.
     *
     * @return the root element of the UI tree
     */
    public abstract Element<?> build();

    @Override
    protected void init() {
        var screen = box()
            .width(this.width)
            .height(this.height);
        screen.children(this.root);
        // Layout again
        new LayoutEngine().layout(screen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        // Render the tree
        root.renderTree(new RenderContext(guiGraphics, mouseX, mouseY, delta));
    }

    @Override
    public void onClose() {
        this.back();
    }
}
