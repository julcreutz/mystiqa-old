package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import game.loader.*;
import game.main.site.SiteState;
import game.main.world_map.WorldMapState;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Mystiqa";

	public static final int WIDTH = 128;
	public static final int HEIGHT = 72;
	public static final int SCALE = 12;

    public final WorldMapState WORLD_MAP = new WorldMapState();
    public final SiteState SITE = new SiteState();

    public final GameState[] STATES = new GameState[] {WORLD_MAP, SITE};

	public static float time;

	public GameState state;
	public GameState nextState;

	@Override
	public void create() {
		ShaderProgram.pedantic = false;

		SheetLoader.load();
		ColorLoader.load();
		BiomeLoader.load();
		WorldMapPlayerTypeLoader.load();
		BiomeLoader.load();
		TileLoader.load();

		WORLD_MAP.create();
		SITE.create();
		state = WORLD_MAP;
	}

	@Override
	public void resize(int width, int height) {
		for (GameState state : STATES) {
			state.resize(width, height);
		}
	}

	@Override
	public void render() {
		if (nextState != null) {
			state = nextState;
			nextState = null;
		}

		time += delta();

		if (state != null) {
			state.update(this);
			state.render();
		}
	}

	@Override
	public void dispose() {
		SheetLoader.dispose();

		for (GameState state : STATES) {
			state.dispose();
		}
	}

	public static float delta() {
		return Gdx.graphics.getDeltaTime();
	}

	public static void write(SpriteBatch batch, String text, float x, float y, boolean centered) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int val = (int) c - 32;
			batch.draw(SheetLoader.load("Font")[val % 10][MathUtils.floor(val / 10f)], x + i * 4 - (centered ? text.length() * 2 : 0), y);
		}
	}
}
