package mystiqa.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import mystiqa.Perlin;
import mystiqa.main.screen.Play;
import mystiqa.main.screen.Screen;

public class Game extends ApplicationAdapter {
	public static float time;

	public Screen screen;

	@Override
	public void create() {
		ShaderProgram.pedantic = false;

		Play.getInstance().create();
		screen = Play.getInstance();
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void render() {
		time += getDelta();

		screen.update();
		screen.renderMaster();
	}

	@Override
	public void dispose() {
		screen.dispose();
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}

	public static FrameBuffer createFrameBuffer() {
		FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, 128, 72, false);
		fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		return fb;
	}

}
