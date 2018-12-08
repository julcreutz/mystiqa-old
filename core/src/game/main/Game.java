package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import game.loader.ColorLoader;
import game.loader.SheetLoader;
import game.loader.WorldMapPlayerTypeLoader;
import game.loader.WorldMapTileTypeLoader;
import game.main.world_map.WorldMap;

import java.util.HashMap;

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
		ShaderProgram.pedantic = false;

		SheetLoader.load();
		ColorLoader.load();
		WorldMapTileTypeLoader.load();
		WorldMapPlayerTypeLoader.load();

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
		SheetLoader.dispose();

		WORLD_MAP.dispose();
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}
}
