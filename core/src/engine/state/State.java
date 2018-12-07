package engine.state;

/**
 * Base class for states.
 */
public interface State {
    /**
     * Called when state is created. Use this to
     * nullify current state.
     * Immediately call this when instantiating.
     */
    void create();

    /**
     * Called when state should be disposed.
     */
    void dispose();

    /**
     * Called when state is being switched to.
     */
    void onEnter();

    /**
     * Called when another state is switched to.
     */
    void onLeave();
}
