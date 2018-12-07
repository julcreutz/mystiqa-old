package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Mystiqa";

	public static final int WIDTH = 128;
	public static final int HEIGHT = 72;
	public static final int SCALE = 12;

	public static float time;

	public GameState state;

	@Override
	public void create() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		time += getDelta();

		if (state != null) {
			state.update(this);
			state.render(this);
		}
	}

	@Override
	public void dispose() {
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}
}
