package game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Play game state. This is the game.
 *
 * @see GameState
 */
public class Play implements GameState {
    /**
     * Default sprite batch used for rendering.
     */
    private SpriteBatch batch;

    /**
     * Default camera for orthographic projection.
     */
    private OrthographicCamera cam;

    /**
     * Default viewport to make the game fit
     * different monitor aspect ratios.
     */
    private FitViewport viewport;

    /**
     * Default frame buffer to scale the game
     * up to window size. This gets rid of pixel
     * interpolation, especially noticeable at high
     * window resolutions.
     */
    private FrameBuffer buffer;

    @Override
    public void update(Game g) {
        cam.update();
    }

    @Override
    public void render(Game g) {
        buffer.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.end();

        buffer.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(buffer.getColorBufferTexture(), cam.position.x - buffer.getWidth() * .5f, cam.position.y + buffer.getHeight() * .5f, buffer.getWidth(), -buffer.getHeight());
        batch.end();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);

        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Game.WIDTH, Game.HEIGHT, false);
        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, cam);
        viewport.apply();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }
}
