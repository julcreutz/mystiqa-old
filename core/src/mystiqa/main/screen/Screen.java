package mystiqa.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import mystiqa.main.Game;

public class Screen {
    public FrameBuffer fb;
    public SpriteBatch batch;
    public OrthographicCamera cam;
    public FitViewport viewport;

    public void create() {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 256, 144);
        viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight, cam);
        viewport.apply();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        fb = Game.createFrameBuffer();
    }

    public void update() {

    }

    public final void renderMaster() {
        fb.begin();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        render(batch);
        batch.end();

        fb.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(fb.getColorBufferTexture(), cam.position.x - 128, cam.position.y - 72 + fb.getHeight(), fb.getWidth(), -fb.getHeight());
        batch.end();
    }

    public void render(SpriteBatch batch) {

    }

    public void dispose() {
        fb.dispose();
        batch.dispose();
    }
}
