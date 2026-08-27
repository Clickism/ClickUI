package de.clickism.clickui;

import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.util.Util;
import net.minecraft.client.Minecraft;
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
    private Element<?> root;

    /**
     * The parent screen of this UiScreen, if any.
     * This can be used to navigate back to the previous screen.
     */
    private @Nullable UiScreen parent;

    /**
     * Indicates whether debug mode is enabled for this UiScreen.
     * When enabled, additional debug information may be rendered.
     */
    private boolean debug = false;

    /**
     * Whether the background of the screen should be rendered.
     */
    private boolean background = true;

    /**
     * Creates a new UiScreen with the specified title component.
     *
     * @param component the title component of the screen
     */
    public UiScreen(Component component) {
        super(component);
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
     * Returns the current UiScreen if the current screen is an instance of UiScreen, otherwise returns null.
     *
     * @return the current UiScreen or null if the current screen is not a UiScreen
     */
    public static @Nullable UiScreen current() {
        var screen = Minecraft.getInstance().screen;
        if (screen instanceof UiScreen uiScreen) {
            return uiScreen;
        }
        return null;
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
     * Sets whether the background of the screen should be rendered.
     *
     * @param background true to render the background, false to not render it
     */
    public void background(boolean background) {
        this.background = background;
    }

    /**
     * Opens the specified UiScreen, setting this screen as its parent.
     *
     * @param screen the UiScreen to open
     */
    public void forwardTo(UiScreen screen) {
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
     * Enables or disables debug mode for this UiScreen.
     *
     * @param debug true to enable debug mode, false to disable it
     */
    public void debug(boolean debug) {
        this.debug = debug;
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

    /**
     * Initializes the UI tree by calling the build method and setting up the root element.
     */
    private void initialize() {
        if (this.root != null) {
            // Already initialize
            return;
        }
        var root = build();
        if (root != null) {
            this.root = root;
        } else {
            // Empty fallback element
            this.root = box();
        }
    }

    @Override
    protected void init() {
        initialize();
        // Initialize all elements anyways
        Util.preOrder(this.root, Element::initialize);
        // Layout the root element
        var screen = box()
            .width(this.width)
            .height(this.height);
        screen.children(this.root);
        // Layout again
        new LayoutEngine().layout(screen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // Render background if enabled
        if (background) {
            this.renderBackground(guiGraphics);
        }
        // Lay out root again if dirty
        if (root.isDirty()) {
            init();
            // Clear dirty state
            root.clearDirty();
        }
        // Call event handler
        super.render(guiGraphics, mouseX, mouseY, delta);
        // Render the tree
        root.renderTree(new RenderContext(guiGraphics, mouseX, mouseY, delta, debug));
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.fillGradient(0, 0, this.width, this.height, -0x4FEFEFF0, -0x3FEFEFF0);
    }

    @Override
    public void tick() {
        // Tick all elements in the tree
        Util.preOrder(root, Element::tick);
    }

    @Override
    public void onClose() {
        this.back();
    }
}
