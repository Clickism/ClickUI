package de.clickism.clickui;

import de.clickism.clickui.render.RenderContext;
import de.clickism.clickui.util.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class UiScreen<S extends UiScreen<S>> extends UiComponent<S>
    implements UiScreenControls {
    /**
     * The title component of this UiScreen.
     */
    private Component title = Component.empty();

    /**
     * Whether the background of the screen should be rendered.
     */
    private boolean background = true;

    /**
     * Returns the title component of this UiScreen.
     *
     * @return the title component
     */
    public Component title() {
        return title;
    }

    /**
     * Sets the title component of this UiScreen.
     *
     * @param title the title component to set
     * @return this UiScreen instance for method chaining
     */
    public UiScreen<S> title(Component title) {
        this.title = title;
        return this;
    }

    /**
     * Sets whether the background of the screen should be rendered.
     *
     * @param background true to render the background, false to not render it
     * @return this UiScreen instance for method chaining
     */
    public UiScreen<S> background(boolean background) {
        this.background = background;
        return this;
    }

    @Override
    public Screen screenToOpen() {
        return new UiScreenHandler(this);
    }

    @Override
    public void render(RenderContext context) {
        // Render the background if enabled
        if (background) {
            renderBackground(context);
        }
        super.render(context);
    }

    /**
     * Renders the background of the screen.
     *
     * @param context the render context to use for rendering the background
     */
    public void renderBackground(RenderContext context) {
        context.graphics().fillGradient(0, 0, context.screenWidth(), context.screenHeight(), -0x4FEFEFF0, -0x3FEFEFF0);
    }

    /**
     * Called when the screen is closed.
     */
    public void handleClose() {
        // Try to go back by default
        this.close();
    }

    /**
     * Opens a UiElement as a screen in the Minecraft client.
     *
     * @param root the root element of the UI tree to be displayed as a screen
     */
    public static void openAsScreen(UiElement<?> root) {
        Util.openScreen(new UiScreenHandler(root));
    }

    /**
     * Wraps a UiElement in a UiScreen, allowing it to be used as a screen in the Minecraft client.
     * <p>
     * When possible, better to use {@link UiScreen#openAsScreen(UiElement)}.
     *
     * @param root the root element of the UI tree to be wrapped as a screen
     * @return a UiScreen that wraps the specified UiElement
     */
    public static UiScreen<? extends UiScreen<?>> asScreen(UiElement<?> root) {
        return new UiElementScreen(root);
    }

    /**
     * A UiScreen that wraps a UiElement, allowing it to be used as a screen in the Minecraft client.
     */
    private static class UiElementScreen extends UiScreen<UiElementScreen> {
        private final UiElement<?> root;

        private UiElementScreen(UiElement<?> root) {
            this.root = root;
        }

        @Override
        protected void build() {
            // Grow to take up same space as screen
            // So that the root acts like a screen and can be used as a screen
            grow();
            // Add root as usual
            add(root);
        }
    }
}
