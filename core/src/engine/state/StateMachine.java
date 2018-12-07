package engine.state;

/**
 * Base class for game state machines.
 * Use type parameter T to specify type of state, e.g.
 * game state, player state, etc.
 */
public interface StateMachine<T extends State> {
    /**
     * Called when state machine is created.
     * Immediately call this when instantiating.
     */
    void create();

    /**
     * Called when state machine should be disposed.
     */
    void dispose();

    /**
     * @return current state
     */
    T getState();

    /**
     * When switching state, both {@link State#onLeave()} of
     * former state, if former state is not null, and
     * {@link State#onEnter()} of new state are called.
     *
     * @param s state to switch to
     */
    void setState(T s);
}
