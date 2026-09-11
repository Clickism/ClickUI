package de.clickism.clickui;

import de.clickism.clickui.reactivity.State;
import de.clickism.clickui.render.RenderContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Supplier;

/**
 * Components are the reactive layer of the UI.
 * They are not rendered directly, instead they are used to build the UI tree and manage state.
 * <p>
 * To trigger a rebuild of a component (i.e. when state changes),
 * call {@link #invalidateTree()} instead of {@link #invalidateLayout()}.
 * <p>
 * To ensure that a component works correctly, there are some rules that must be followed:
 * <ul>
 *     <li>All event listeners must be registered in the {@link #build()} method.</li>
 *     <li>The order of {@link #memo(Supplier)} calls without keys must be consistent,
 *         or invalidated via {@link #clearMemo()}.</li>
 * </ul>
 *
 * @param <S> The self type of the component
 */
// TODO: Keep focus after rebuild?
// TODO: Rename to UiComponent to not confuse with text components?
public abstract class UiComponent<S extends UiComponent<S>> extends UiElement<S>
    implements BaseComponents {

    private int memoIndex = 0;
    private final List<Object> indexMemoized = new ArrayList<>();
    private final Map<Object, Object> keyMemoized = new HashMap<>();
    private final Set<Object> usedMemoKeys = new HashSet<>();

    /**
     * Indicates whether the component is marked as dirty and needs to be rebuilt.
     * <p>
     * True by defualt, so that the component is built when first created.
     */
    private boolean dirtyTree = true;

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
     * Invalidates this component's element tree.
     * <p>
     * This will cause the component to be rebuilt on the next render cycle.
     */
    public final void invalidateTree() {
        this.dirtyTree = true;
    }

    /**
     * Rebuilds the component if it is marked as dirty.
     * <p>
     * This method is called by the UI framework during the render cycle.
     */
    @ApiStatus.Internal
    public final void rebuildIfNeeded() {
        if (!dirtyTree) return;

        dirtyTree = false;
        memoIndex = 0;
        usedMemoKeys.clear();

        // Clear all event listeners, as they will be re-registered during the build
        events().clearListeners();
        clear();
        build();

        // Remove unused memoized keys
        keyMemoized.keySet().retainAll(usedMemoKeys);

        invalidateLayout();
    }

    // TODO: Make it so that invalidate marks the component as invalid, and only invalid components gets rebuilt when the root is invalid, and the rest gets relayed out
    // TODO: If after rebuild, the bounds are different, then the parent should be invalidated and relayouted! Not REBUILT!

    @Override
    public void render(RenderContext context) {
        // No render by default in components
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
        return new State<>(initialValue, value -> this.invalidateTree());
    }

    /**
     * Memoizes the result of a supplier function for the current build cycle.
     * <p>
     * <strong>
     * Warning! Order of memoization calls matter and should NOT differ between rebuilds.
     * </strong>
     *
     * @param supplier the supplier function to memoize
     * @param <T>      the type of the value to memoize
     * @return the memoized value
     */
    @SuppressWarnings("unchecked")
    protected <T> T memo(Supplier<T> supplier) {
        if (memoIndex < indexMemoized.size()) {
            // Return the existing value
            return (T) indexMemoized.get(memoIndex++);
        } else {
            // Compute the value and store it
            T value = supplier.get();
            indexMemoized.add(value);
            memoIndex++;
            return value;
        }
    }

    /**
     * Memoizes the result of a supplier function for the current build cycle, using a key.
     * <p>
     * This is useful for memoizing values that are not tied to the order of calls,
     * but rather to a specific key.
     *
     * @param key      the key to use for memoization
     * @param supplier the supplier function to memoize
     * @param <K>      the type of the key
     * @param <T>      the type of the value to memoize
     * @return the memoized value
     */
    @SuppressWarnings("unchecked")
    protected <K, T> T memo(K key, Supplier<T> supplier) {
        usedMemoKeys.add(key);
        if (keyMemoized.containsKey(key)) {
            return (T) keyMemoized.get(key);
        } else {
            T value = supplier.get();
            keyMemoized.put(key, value);
            return value;
        }
    }

    /**
     * Clears the index-memoization cache for this component.
     */
    protected void clearMemo() {
        indexMemoized.clear();
        memoIndex = 0;
    }

    /**
     * Clears the key-memoization cache for this component, but only for key-based memoization.
     */
    protected void clearMemoKeys() {
        keyMemoized.clear();
    }
}
