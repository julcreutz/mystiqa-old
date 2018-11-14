package mystiqa.world_generation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import mystiqa.Perlin;
import mystiqa.Resources;
import mystiqa.entity.tile.Grass;
import mystiqa.entity.tile.Tile;
import mystiqa.entity.tile.Water;

public class WorldGenerator extends ApplicationAdapter {
    public SpriteBatch batch;

    private TextureRegion pixel;

    private Color[][] colors;

    @Override
    public void create() {
        batch = new SpriteBatch();

        pixel = Resources.getSpriteSheet("graphics/debug/hitbox.png", 1, 1)[0][0];

        int w = 256 * 3;
        int h = 256 * 3;

        colors = new Color[w][h];
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            generateHeightmap();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.begin();

        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                Color c = colors[x][y];

                if (c != null) {
                    batch.setColor(c.r, c.g, c.b, 1);
                    batch.draw(pixel, x, y);
                }
            }
        }

        batch.end();
    }

    public void generateHeightmap() {
        Perlin perlin = new Perlin();

        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                float noise = perlin.layeredNoise(x * .0075f, y * .0075f, 8, 1, 4f, 1, 1 / 4f);

                if (noise > .45f) {
                    if (noise > .65f) {
                        colors[x][y] = Color.WHITE;
                    } else if (noise > .6f) {
                        colors[x][y] = Color.GRAY;
                    } else if (noise > .55f) {
                        colors[x][y] = Color.LIGHT_GRAY;
                    } else if (noise > .5f) {
                        colors[x][y] = Color.FOREST;
                    } else if (noise > .46f) {
                        colors[x][y] = Color.GREEN;
                    } else {
                        colors[x][y] = Color.YELLOW;
                    }
                } else {
                    colors[x][y] = Color.BLUE;
                }

                int zz = (int) (noise * 128f);
                Tile t = zz < 58 ? new Water() : new Grass();
                colors[x][y] = new Color(t.topColor.r / 255f, t.topColor.g / 255f, t.topColor.b / 255f, 1);
            }
        }
    }
}
