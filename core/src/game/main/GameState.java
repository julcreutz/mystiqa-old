package game.main;

import engine.state.State;

/**
 * State type used by {@Link Game}
 *
 * @see Game
 */
public interface GameState extends State {
    /**
     * Used for updating. This is called every frame.
     *
     * @param g game instance
     */
    void update(Game g);

    /**
     * Used for rendering purposes. This is called
     * every frame.
     *
     * @param g game instance
     */
    void render(Game g);

    /**
     * Called when window is resized.
     * @param w new window width
     * @param h new window height
     */
    void resize(int w, int h);
}
