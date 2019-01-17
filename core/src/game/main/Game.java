package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import game.loader.*;
import game.loader.instance.EntityLoader;
import game.loader.instance.ItemLoader;
import game.loader.instance.MapLoader;
import game.loader.resource.shader.ShaderLoader;
import game.loader.resource.sprite_sheet.SpriteSheetLoader;
import game.loader.instance.TileLoader;
import game.main.state.play.Play;
import game.main.state.GameState;

import java.util.Random;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Mystiqa";
	public static final String VERSION = "0.0";

	public static final int WIDTH = 128;
	public static final int HEIGHT = 72;
	public static final int SCALE = 12;

	public static final Random RANDOM = new Random();

	public static final ShaderLoader SHADERS = new ShaderLoader();
	public static final SpriteSheetLoader SPRITE_SHEETS = new SpriteSheetLoader();
	public static final TileLoader TILES = new TileLoader();
	public static final ItemLoader ITEMS = new ItemLoader();
	public static final EntityLoader ENTITIES = new EntityLoader();
	public static final MapLoader MAPS = new MapLoader();

	public static final Loader[] LOADERS = new Loader[] {
			SPRITE_SHEETS, TILES, ITEMS, ENTITIES, MAPS
	};

    public final Play PLAY = new Play();

    public final GameState[] STATES = new GameState[] {PLAY};

	public static float time;

	public GameState state;
	public GameState nextState;

	@Override
	public void create() {
		ShaderProgram.pedantic = false;

		SHADERS.load(Gdx.files.internal("data/shaders.json"));
		SPRITE_SHEETS.load(Gdx.files.internal("data/sprite_sheets.json"));
		TILES.load(Gdx.files.internal("data/tiles.json"));
		ITEMS.load(Gdx.files.internal("data/items.json"));
		ENTITIES.load(Gdx.files.internal("data/entities.json"));
		MAPS.load(Gdx.files.internal("data/maps.json"));

		PLAY.create();
		state = PLAY;
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

		time += getDelta();

		if (state != null) {
			state.update(this);
			state.render();
		}
	}

	@Override
	public void dispose() {
		for (GameState state : STATES) {
			state.dispose();
		}

		for (Loader l : LOADERS) {
			if (l instanceof Disposable) {
				((Disposable) l).dispose();
			}
		}
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}

	public static void write(SpriteBatch batch, String text, float x, float y, boolean centered) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int val = (int) c - 32;
			batch.draw(Game.SPRITE_SHEETS.load("Font").sheet[val % 10][MathUtils.floor(val / 10f)],
					x + i * 4 - (centered ? text.length() * 2 : 0), y);
		}
	}
}
