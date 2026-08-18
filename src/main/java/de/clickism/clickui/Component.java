package de.clickism.clickui;

import de.clickism.clickui.reactivity.State;
import de.clickism.clickui.render.RenderContext;
import org.jetbrains.annotations.ApiStatus;

/**
 * Components are the reactive layer of the UI.
 * They are not rendered directly, instead they are used to build the UI tree and manage state.
 * <p>
 * To trigger a rebuild of a component (i.e. when state changes),
 * call {@link #rebuild()} instead of {@link #invalidate()}.
 *
 * @param <S> The self type of the component
 */
public abstract class Component<S extends Component<S>> extends Element<S>
    implements UiBuilder {

    /**
     * Builds the UI tree for this component.
     * <p>
     * This method is called when the component is first created,
     * and whenever the component's state changes, and it needs to be rebuilt.
     * <p>
     * It's always called after clearing all children.
     */
    protected abstract void build();

    /**
     * Rebuilds the UI tree for this component.
     */
    @ApiStatus.Internal
    public final void rebuild() {
        super.invalidate();
        clear();
        build();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException("Components cannot be invalidated directly. Use rebuild() instead.");
    }

    @Override
    public void render(RenderContext context) {
        // No render by default in components
    }

    @Override
    public void initialize() {
        // Build for the first time here, to avoid calling build() in the constructor
        // which can lead to issues with subclass initialization.
        build();
    }

    /**
     * Creates a new state variable that is tied to this component.
     * <p>
     * When the state variable is updated, the component will be rebuilt.
     *
     * @param initialValue the initial value of the state variable
     * @param <T>          the type of the state variable
     * @return a new state variable that is tied to this component
     */
    protected <T> State<T> state(T initialValue) {
        return new State<>(initialValue, this);
    }
}
