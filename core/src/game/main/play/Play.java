package game.main.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.GameState;
import game.main.gen.WorldGenerator;
import game.main.play.entity.Entity;
import game.main.play.tile.Tile;
import game.main.play.tile.TileType;

public class Play extends GameState {
    public Tile[][][] tiles;
    public Rectangle[][] solidTiles;

    public Array<Entity> entities;

    public Entity player;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public WorldGenerator worldGenerator;

    @Override
    public void create() {
        super.create();

        worldGenerator = new WorldGenerator(this);
        worldGenerator.generate();
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            worldGenerator.generate();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= 64 * Game.delta();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += 64 * Game.delta();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.position.y -= 64 * Game.delta();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.position.y += 64 * Game.delta();
        }

        //cam.position.x = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        //cam.position.y = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / Game.HEIGHT) * Game.HEIGHT;
        cam.update();

        x0 = MathUtils.clamp(MathUtils.floor(cam.position.x / 8f) - 10, 0, tiles.length);
        x1 = MathUtils.clamp(MathUtils.floor(cam.position.x / 8f) + 10, 0, tiles.length);
        y0 = MathUtils.clamp(MathUtils.floor(cam.position.y / 8f) - 8, 0, tiles[0].length);
        y1 = MathUtils.clamp(MathUtils.floor(cam.position.y / 8f) + 8, 0, tiles[0].length);

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

        for (Entity entity : entities) {
            entity.update(this);
        }
    }

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                Tile tile = tiles[x][y][0];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }

        for (Entity entity : entities) {
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
