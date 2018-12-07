package engine.state;

/**
 * Default state machine implementation.
 *
 * @see StateMachine
 */
public class DefaultStateMachine<T extends State> implements StateMachine<T> {
    private T state;

    public DefaultStateMachine() {
        create();
    }

    @Override
    public void create() {
        state = null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public T getState() {
        return state;
    }

    @Override
    public void setState(T s) {
        if (state != null) {
            state.onLeave();
        }

        state = s;
        state.onEnter();
    }
}
