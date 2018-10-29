package mystiqa.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import mystiqa.main.screen.PlayScreen;
import mystiqa.main.screen.Screen;

public class Game extends ApplicationAdapter {
	public Array<Screen> screens;
	public int currScreen;

	@Override
	public void create() {
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
		FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, 256, 144, false);
		fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		return fb;
	}

	public static void getFiles(FileHandle root, Array<FileHandle> files) {
		for (FileHandle file : root.list()) {
			if (file.isDirectory()) {
				getFiles(file, files);
			} else {
				files.add(file);
			}
		}
	}
}
