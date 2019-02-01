package game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import game.main.item.equipment.hand.right.melee_weapon.BattleAxe;
import game.main.state.play.map.Cave;
import game.resource.Shaders;
import game.resource.Textures;
import game.main.state.play.Play;
import game.main.state.GameState;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Mystiqa";
	public static final String VERSION = "0.0";

	public static final int WIDTH = 80;
	public static final int HEIGHT = 72;

	public static final int SCALE = 12;

	public static final Random RANDOM = new Random();

	public static final Textures TEXTURES = new Textures();
	public static final Shaders SHADERS = new Shaders();

	public static final Play PLAY = new Play();
    public static final GameState[] STATES = new GameState[] {PLAY};

	public static float time;

	public GameState state;
	public GameState nextState;

	@Override
	public void create() {
		ShaderProgram.pedantic = false;

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

		TEXTURES.dispose();
		SHADERS.dispose();
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}
}
