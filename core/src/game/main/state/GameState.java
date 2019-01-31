package game.main.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.SpriteSheet;
import game.main.Game;

public abstract class GameState {
    public SpriteBatch batch;
    public OrthographicCamera cam;
    public FitViewport viewport;

    public FrameBuffer buffer;

    public SpriteSheet font;

    public void create() {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, cam);
        viewport.apply();

        buffer = createFrameBuffer();

        font = new SpriteSheet("font", 10, 10);
    }

    public void update(Game g) {
        cam.update();
    }

    public void render() {
        buffer.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        renderToBuffer();
        batch.end();

        buffer.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Makes the viewport work
        buffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        renderBuffer(buffer);
        batch.end();
    }

    public void renderToBuffer() {
    }

    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    public void dispose() {
        batch.dispose();
        buffer.dispose();
    }

    public FrameBuffer createFrameBuffer() {
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Game.WIDTH, Game.HEIGHT, false);
        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        return buffer;
    }

    public void renderBuffer(FrameBuffer buffer) {
        batch.draw(buffer.getColorBufferTexture(),
                cam.position.x - buffer.getWidth() * .5f, cam.position.y + buffer.getHeight() * .5f,
                buffer.getWidth(), -buffer.getHeight());
    }

    public void write(String text, float x, float y, boolean centered) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int val = (int) c - 32;
            batch.draw(font.grab(val % 10, MathUtils.floor(val / 10f)),
                    x + i * 4 - (centered ? text.length() * 2 : 0), y);
        }
    }
}
