package mystiqa.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import mystiqa.Perlin;
import mystiqa.main.screen.PlayScreen;
import mystiqa.main.screen.Screen;

import java.util.HashMap;

public class Game extends ApplicationAdapter {
	public static HashMap<Color, ShaderProgram> absoluteShaders;
	public static HashMap<Color, ShaderProgram> relativeShaders;

	public static float time;

	public Array<Screen> screens;
	public int currScreen;

	@Override
	public void create() {
		ShaderProgram.pedantic = false;

		screens = new Array<Screen>();

		PlayScreen p = new PlayScreen();
		p.create();
		screens.add(p);
	}

	@Override
	public void resize(int width, int height) {
		for (Screen s : screens) {
			s.resize(width, height);
		}
	}

	@Override
	public void render() {
		time += getDelta();

		screens.get(currScreen).update();
		screens.get(currScreen).renderMaster();
	}

	@Override
	public void dispose() {
		for (Screen s : screens) {
			s.dispose();
		}
	}

	public static float getDelta() {
		return Gdx.graphics.getDeltaTime();
	}

	public static FrameBuffer createFrameBuffer() {
		FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, 128, 72, false);
		fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		return fb;
	}

	public static Array<FileHandle> getFiles(FileHandle root) {
		Array<FileHandle> files = new Array<FileHandle>();
		getFiles(root, files);
		return files;
	}

	private static void getFiles(FileHandle root, Array<FileHandle> files) {
		for (FileHandle file : root.list()) {
			if (file.isDirectory()) {
				getFiles(file, files);
			} else {
				files.add(file);
			}
		}
	}
}
