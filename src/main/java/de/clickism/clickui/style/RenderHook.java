package de.clickism.clickui.style;

import de.clickism.clickui.UiElement;
import de.clickism.clickui.render.RenderContext;

/**
 * Represents a render hook that can be executed before or after rendering a UI element.
 *
 * @param type     The type of the render hook, indicating when it should be executed (PRE or POST_RENDER).
 * @param renderer The renderer that will be called during the rendering process.
 */
public record RenderHook(
    Type type,
    Renderer renderer
) {
    /**
     * The type of the render hook, indicating when it should be executed.
     */
    public enum Type {
        /**
         * Indicates that the hook should be run before the element is rendered.
         */
        PRE,
        /**
         * Indicates that the hook should be run after the element is rendered.
         */
        POST
    }

    /**
     * Functional interface for rendering a UI element within a given render context.
     */
    @FunctionalInterface
    public interface Renderer {
        void render(RenderContext context, UiElement<?> element);
    }
}
