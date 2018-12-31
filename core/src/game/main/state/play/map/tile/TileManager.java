package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import game.main.state.play.map.Map;

/**
 * A helper class that manages, constructs and generally
 * handles tiles.
 *
 * Tiles are stored in a three dimensional array, where the
 * first dimension is x, the second y and the third z,
 * which is basically three dimensional "depth" in a two
 * dimensional world. The tiles' y offset depends on their
 * z coordinate to give an impression of space.
 */
public class TileManager {
    /** Holds reference to map. */
    public Map map;

    /** Stores tiles. */
    public Tile[][][] tiles;

    /** Stores solid tile "hitboxes". */
    public Rectangle[][] solidTiles;

    public TileManager(Map map) {
        this.map = map;
    }

    /**
     * Updates all tiles in a given area. If a tile is newly created
     * it will be updated even when the camera is moving to prevent missing textures.
     *
     * @param x0 start x (inclusive
     * @param x1 end x (exclusive)
     * @param y0 start y (inclusive)
     * @param y1 end y (exclusive)
     */
    public void update(int x0, int x1, int y0, int y1) {
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
                        if (!tile.updated) {
                            tile.update(map);
                            tile.updated = true;
                        } else if (!map.isCamMoving()) {
                            tile.update(map);
                        }
                    }
                }
            }
        }
    }

    /**
     * Renders tiles in a given area.
     *
     * @param batch sprite batch to use
     * @param x0 start x (inclusive)
     * @param x1 end x (exclusive)
     * @param y0 start y (inclusive)
     * @param y1 end y (exclusive)
     * @param z0 start z (inclusive)
     * @param z1 end z (exclusive)
     */
    public void render(SpriteBatch batch, int x0, int x1, int y0, int y1, int z0, int z1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                for (int z = z0; z < z1; z++) {
                    Tile tile = tiles[x][y][z];

                    if (tile != null) {
                        tile.render(batch);
                    }
                }
            }
        }
    }

    /**
     * Checks whether given coordinates are within tile bounds.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return whether coordinates are in bounds
     */
    public boolean inBounds(int x, int y, int z) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() && z >= 0 && z < getDepth();
    }

    /**
     * Convenience method not regarding z to check whether given coordinates are
     * within tiles bounds. Uses {@link #inBounds(int, int, int)} for validation
     * and uses zero as z coordinate.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return whether coordinates are in bounds
     */
    public boolean inBounds(int x, int y) {
        return inBounds(x, y, 0);
    }

    /**
     * Returns tile at given coordinates as long as they are within
     * bounds. If not, an {@link IndexOutOfBoundsException} is thrown.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return tile at given coordinates
     */
    public Tile tileAt(int x, int y, int z) {
        if (!inBounds(x, y, z)) {
            throw new IndexOutOfBoundsException();
        }

        return tiles[x][y][z];
    }

    public void placeTile(Tile tile, int x, int y, int z) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0].length) {
            tile.x = x;
            tile.y = y;
            tile.z = z;
            tiles[x][y][z] = tile;
        }
    }

    public void placeTile(Tile.Type type, int x, int y, int z) {
        placeTile(new Tile(type), x, y, z);
    }

    public void erase(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            for (int z = 0; z < tiles[0][0].length; z++) {
                tiles[x][y][z] = null;
            }
        }
    }

    public boolean isFree(int x, int y, int z, int r) {
        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                if (inBounds(x + xx, y + yy, z)) {
                    Tile t = tileAt(x + xx, y + yy, z);

                    if (t != null && t.type.solid) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public void initSize(int w, int h, int d) {
        tiles = new Tile[w][h][d];
        solidTiles = new Rectangle[w][h];
    }

    public int getDepth() {
        return tiles[0][0].length;
    }
}
