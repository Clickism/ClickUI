package de.clickism.clickui.testmod;

import de.clickism.clickui.Element;
import de.clickism.clickui.UiBuilder;
import de.clickism.clickui.render.RenderContext;

public class Counter extends Element<Counter> implements UiBuilder {
    private int count = 0;

    public Counter() {
        rebuild();
    }

    public void increment() {
        count++;
        rebuild();
    }

    public void rebuild() {
        invalidate();
        clear();
        add(text("Count: " + count));
        add(memorize(
            button("Increment").onClick(event -> this.increment())
        ));
    }

    private Object memod = null;

    @SuppressWarnings("unchecked")
    protected <T> T memorize(T element) {
        if (memod == null) {
            memod = element;
        }
        return (T) memod;
    }

    @Override
    public void render(RenderContext context) {

    }
}
