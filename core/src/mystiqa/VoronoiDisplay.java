package mystiqa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import mystiqa.world.Chunk;
import mystiqa.world.WorldGenerator;

public class VoronoiDisplay extends ApplicationAdapter implements InputProcessor {
    private Color[][] colors;
    private SpriteBatch batch;

    private float scale;

    private float mouseX;
    private float mouseY;

    private float ox;
    private float oy;

    @Override
    public void create() {
        batch = new SpriteBatch();

        colors = new Color[Gdx.graphics.getWidth()][Gdx.graphics.getHeight()];

        scale = 1;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        WorldGenerator worldGenerator = Assets.getInstance().getWorldGenerator("WorldGenerator");
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                int xx = x * Chunk.WIDTH;
                int yy = y * Chunk.HEIGHT;

                colors[x][y] = new Color(worldGenerator.get(xx, yy, MathUtils.clamp(worldGenerator.getHeight(xx, yy), worldGenerator.waterLevel, Integer.MAX_VALUE)).topColor);
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                if (colors[x][y] != null) {
                    batch.setColor(colors[x][y]);
                    batch.draw(Assets.getInstance().getSpriteSheet("Hitbox")[0][0], x, y);
                }
            }
        }
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseX = screenX;
        mouseY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        ox -= screenX - mouseX;
        oy += screenY - mouseY;

        mouseX = screenX;
        mouseY = screenY;

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        scale -= amount;

        if (amount < 0) {
            ox += (1f / scale) * Gdx.graphics.getWidth();
            oy += (1f / scale) * Gdx.graphics.getHeight();
        } else {
            ox -= (1f / scale) * Gdx.graphics.getWidth();
            oy -= (1f / scale) * Gdx.graphics.getHeight();
        }

        return true;
    }
}
