package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import engine.state.DefaultStateMachine;
import engine.state.StateMachine;

/**
 * This is where the game originates from. It
 * controls the different game states.
 */
public class Game extends ApplicationAdapter {
	/**
	 * Current game version. Update accordingly.
	 */
	public static final String VERSION = "v0.1";

	/**
	 * Game title.
	 */
	public static final String TITLE = "Mystiqa " + VERSION;

	/**
	 * Standard width of game in pixels.
	 */
	public static final int WIDTH = 128;

	/**
	 * Standard height of game in pixels.
	 */
	public static final int HEIGHT = 72;

	/**
	 * Window scale.
	 */
	public static final int SCALE = 12;

	/**
	 * Holds current time in seconds.
	 */
	private static float time;

	/**
	 * State machine used to differentiate different
	 * game states.
	 */
	private StateMachine<GameState> stateMachine;

	/**
	 * The play state. Instantiate before setting.
	 */
	private Play play;

	@Override
	public void create() {
		play = new Play();
		play.create();

		stateMachine = new DefaultStateMachine<GameState>();
		stateMachine.setState(play);
	}

	@Override
	public void resize(int width, int height) {
		play.resize(width, height);
	}

	@Override
	public void render() {
		time += getDelta();

		if (stateMachine.getState() != null) {
			stateMachine.getState().update(this);
			stateMachine.getState().render(this);
		}
	}

	@Override
	public void dispose() {
		play.dispose();
		stateMachine.dispose();
	}

	/**
	 * Modify this to slow down/speed up the game.
	 *
	 * @return delta time
	 */
	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}

	/**
	 * @return current time in seconds
	 */
	public static float getTime() {
		return time;
	}
}
