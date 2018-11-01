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
import mystiqa.main.screen.PlayScreen;
import mystiqa.main.screen.Screen;

import java.util.HashMap;

public class Game extends ApplicationAdapter {
	public static HashMap<Color, ShaderProgram> absoluteShaders;
	public static HashMap<Color, ShaderProgram> relativeShaders;

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

	public static ShaderProgram colorToAbsoluteShader(Color c) {
		if (absoluteShaders == null) {
			absoluteShaders = new HashMap<Color, ShaderProgram>();
		}

		if (!absoluteShaders.containsKey(c)) {
			String vert =
					"attribute vec4 a_position;\n" +
							"attribute vec4 a_color;\n" +
							"attribute vec2 a_texCoord0;\n" +
							"\n" +
							"uniform mat4 u_projTrans;\n" +
							"\n" +
							"varying vec4 v_color;\n" +
							"varying vec2 v_texCoords;\n" +
							"\n" +
							"void main() {\n" +
							"    v_color = a_color;\n" +
							"    v_texCoords = a_texCoord0;\n" +
							"\n" +
							"    gl_Position = u_projTrans * a_position;\n" +
							"}";

			String frag =
					"#ifdef GL_ES\n" +
							"    precision mediump float;\n" +
							"#endif\n" +
							"\n" +
							"varying vec4 v_color;\n" +
							"varying vec2 v_texCoords;\n" +
							"uniform sampler2D u_texture;\n" +
							"\n" +
							"uniform float r;\n" +
							"uniform float g;\n" +
							"uniform float b;\n" +
							"\n" +
							"void main() {\n" +
							"    vec4 c = v_color * texture2D(u_texture, v_texCoords);\n" +
							"\n" +
							"    if (c.w > 0) {\n" +
							"        c.x = " + c.r + " / 255.0;\n" +
							"        c.y = " + c.g + " / 255.0;\n" +
							"        c.z = " + c.b + " / 255.0;\n" +
							"    }\n" +
							"\n" +
							"    gl_FragColor = c;\n" +
							"}";

			absoluteShaders.put(c, new ShaderProgram(vert, frag));
		}

		return absoluteShaders.get(c);
	}

	public static ShaderProgram colorToRelativeShader(Color c) {
		if (relativeShaders == null) {
			relativeShaders = new HashMap<Color, ShaderProgram>();
		}

		if (!relativeShaders.containsKey(c)) {
			String vert =
					"attribute vec4 a_position;\n" +
							"attribute vec4 a_color;\n" +
							"attribute vec2 a_texCoord0;\n" +
							"\n" +
							"uniform mat4 u_projTrans;\n" +
							"\n" +
							"varying vec4 v_color;\n" +
							"varying vec2 v_texCoords;\n" +
							"\n" +
							"void main() {\n" +
							"    v_color = a_color;\n" +
							"    v_texCoords = a_texCoord0;\n" +
							"\n" +
							"    gl_Position = u_projTrans * a_position;\n" +
							"}";

			String frag =
					"#ifdef GL_ES\n" +
							"    precision mediump float;\n" +
							"#endif\n" +
							"\n" +
							"varying vec4 v_color;\n" +
							"varying vec2 v_texCoords;\n" +
							"uniform sampler2D u_texture;\n" +
							"\n" +
							"uniform float r;\n" +
							"uniform float g;\n" +
							"uniform float b;\n" +
							"\n" +
							"void main() {\n" +
							"    vec4 c = v_color * texture2D(u_texture, v_texCoords);\n" +
							"\n" +
							"    if (c.w > 0) {\n" +
							"        c.x += " + c.r + " / 255.0;\n" +
							"        c.y += " + c.g + " / 255.0;\n" +
							"        c.z += " + c.b + " / 255.0;\n" +
							"    }\n" +
							"\n" +
							"    gl_FragColor = c;\n" +
							"}";

			relativeShaders.put(c, new ShaderProgram(vert, frag));
		}

		return relativeShaders.get(c);
	}
}
