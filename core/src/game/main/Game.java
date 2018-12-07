package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import game.main.world_map.WorldMap;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Mystiqa";

	public static final int WIDTH = 128;
	public static final int HEIGHT = 72;
	public static final int SCALE = 12;

	public static float time;

	public final WorldMap WORLD_MAP = new WorldMap();

	public GameState state;

	@Override
	public void create() {
		WORLD_MAP.create();
		state = WORLD_MAP;
	}

	@Override
	public void resize(int width, int height) {
		WORLD_MAP.resize(width, height);
	}

	@Override
	public void render() {
		time += getDelta();

		if (state != null) {
			state.update(this);
			state.render();
		}
	}

	@Override
	public void dispose() {
		WORLD_MAP.dispose();
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}
}
