package game.main.site;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.loader.ColorLoader;
import game.loader.SheetLoader;
import game.main.Game;
import game.main.site.entity.Humanoid;
import game.main.site.entity.SiteEntity;
import game.main.site.tile.Tile;
import game.main.site.tile.TileType;

public class SiteData {
    public Tile[][][] tiles;
    public Rectangle[][] solidTiles;

    public Array<SiteEntity> entities;

    public SiteEntity player;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public SiteData() {
        tiles = new Tile[64][64][2];
        solidTiles = new Rectangle[tiles.length][tiles[0].length];

        entities = new Array<SiteEntity>();

        player = new Humanoid();
        player.x = 16;
        player.y = 16;
        ((Humanoid) player).feet = SheetLoader.load("HumanFeet");
        ((Humanoid) player).body = SheetLoader.load("HumanBody");
        ((Humanoid) player).head = SheetLoader.load("HumanHead");
        ((Humanoid) player).color = ColorLoader.load("Peach");
        ((Humanoid) player).animSpeed = 7.5f;

        entities.add(player);
    }

    public void update(SiteState state) {
        state.cam.position.x = MathUtils.clamp(player.x + 4, Game.WIDTH * .5f, tiles.length * 8 - Game.WIDTH * .5f);
        state.cam.position.y = MathUtils.clamp(player.y + 4, Game.HEIGHT * .5f, tiles[0].length * 8 - Game.HEIGHT * .5f);
        state.cam.update();

        x0 = MathUtils.clamp(MathUtils.floor(state.cam.position.x / 8f) - 10, 0, tiles.length);
        x1 = MathUtils.clamp(MathUtils.floor(state.cam.position.x / 8f) + 10, 0, tiles.length);
        y0 = MathUtils.clamp(MathUtils.floor(state.cam.position.y / 8f) - 8, 0, tiles[0].length);
        y1 = MathUtils.clamp(MathUtils.floor(state.cam.position.y / 8f) + 8, 0, tiles[0].length);

        for (int x = 0; x < solidTiles.length; x++) {
            for (int y = 0; y < solidTiles[0].length; y++) {
                Tile t = tiles[x][y][0];

                if (t != null) {
                    if (t.type.solid && solidTiles[x][y] == null) {
                        solidTiles[x][y] = new Rectangle(x * 8, y * 8, 8, 8);
                    } else if (!t.type.solid && solidTiles[x][y] != null) {
                        solidTiles[x][y] = null;
                    }
                }
            }
        }

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
                Tile tile = tiles[x][y][1];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }
    }

    public Tile tileAt(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
    }

    public void placeTile(Tile tile, int x, int y, int z) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0].length) {
            tile.x = x;
            tile.y = y;
            tile.z = z;
            tiles[x][y][z] = tile;
        }
    }

    public void placeTile(TileType type, int x, int y, int z) {
        placeTile(new Tile(type), x, y, z);
    }
}
