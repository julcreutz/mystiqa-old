package game.main.site;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.site.entity.SiteEntity;
import game.main.site.tile.Tile;
import game.main.site.tile.TileType;

public class SiteData {
    public Tile[][][] tiles;
    public Array<SiteEntity> entities;

    public float x;
    public float y;

    public int xx;
    public int yy;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public SiteData() {
        tiles = new Tile[64][64][8];
        entities = new Array<SiteEntity>();
    }

    public void update(SiteState state) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 32 * Game.delta();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 32 * Game.delta();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= 32 * Game.delta();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += 32 * Game.delta();
        }

        state.cam.position.x = x;
        state.cam.position.y = y;
        state.cam.update();

        xx = MathUtils.floor(x / 8f);
        yy = MathUtils.floor(y / 8f);

        x0 = MathUtils.clamp(xx - 10, 0, tiles.length);
        x1 = MathUtils.clamp(xx + 10, 0, tiles.length);
        y0 = MathUtils.clamp(yy - 8, 0, tiles[0].length);
        y1 = MathUtils.clamp(yy + 8, 0, tiles[0].length);

        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                for (int z = 0; z < tiles[0][0].length; z++) {
                    Tile tile = tiles[x][y][z];

                    if (tile != null) {
                        tile.update(this);
                    }
                }
            }
        }

        for (SiteEntity entity : entities) {
            entity.update(this);
        }
    }

    public void render(SpriteBatch batch) {
        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                Tile tile = tiles[x][y][0];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }

        for (SiteEntity entity : entities) {
            entity.render(batch);
        }

        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                for (int z = 1; z < tiles[0][0].length; z++) {
                    Tile tile = tiles[x][y][z];

                    if (tile != null) {
                        tile.render(batch);
                    }
                }
            }
        }
    }

    public Tile tileAt(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
    }

    public void placeTile(Tile tile, int x, int y, int z) {
        tile.x = x;
        tile.y = y;
        tile.z = z;
        tiles[x][y][z] = tile;
    }

    public void placeTile(TileType type, int x, int y, int z) {
        placeTile(new Tile(type), x, y, z);
    }
}
