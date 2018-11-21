package mystiqa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import mystiqa.world.Chunk;
import mystiqa.world.WorldGenerator;

public class VoronoiDisplay extends ApplicationAdapter {
    private Color[][] colors;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        colors = new Color[Gdx.graphics.getWidth()][Gdx.graphics.getHeight()];

        float xRes = (256 * Chunk.WIDTH) / (float) colors.length;
        float yRes = (256 * Chunk.HEIGHT) / (float) colors[0].length;

        WorldGenerator worldGenerator = Assets.getInstance().getWorldGenerator("WorldGenerator");
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                int xx = (int) (x * xRes);
                int yy = (int) (y * yRes);

                Color c = new Color(worldGenerator.get(null, xx, yy, MathUtils.clamp(worldGenerator.getHeight(xx, yy), worldGenerator.waterLevel, Integer.MAX_VALUE)).topColor);

                float elevation = worldGenerator.getElevation(xx, yy);
                c.r *= elevation;
                c.g *= elevation;
                c.b *= elevation;

                colors[x][y] = c;
            }
        }
    }

    @Override
    public void render() {
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
}
