package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import game.main.state.play.map.Map;

/**
 * Helper class that manages, constructs and generally handles tiles.
 *
 * Tiles are stored in a three dimensional array, where the first dimension is x, the second y and the third z, which is
 * basically three dimensional "depth" in a two dimensional world. The tiles' y offset depends on their z coordinate
 * to give an impression of space.
 */
public class TileManager {
    /** Holds reference of map. */
    public Map map;

    /** Stores tiles. */
    public Tile[][][] tiles;

    /** Stores solid tile "hitboxes". */
    public Rectangle[][] solidTiles;

    /**
     * Constructs new instance with map reference.
     *
     * @param map map reference
     */
    public TileManager(Map map) {
        this.map = map;
    }

    /**
     * Updates all tiles in a given area. If a tile is newly created it will be updated even when the camera is moving
     * to prevent missing textures.
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
                    if (t.solid && solidTiles[x][y] == null) {
                        solidTiles[x][y] = new Rectangle(x * 8, y * 8, 8, 8);
                    } else if (!t.solid && solidTiles[x][y] != null) {
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
        return x >= 0 && x < width() && y >= 0 && y < height() && z >= 0 && z < depth();
    }

    /**
     * Convenience method not regarding z to check whether given coordinates are within tiles bounds. Uses
     * {@link #inBounds(int, int, int)} for validation and uses zero as z coordinate.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return whether coordinates are in bounds
     */
    public boolean inBounds(int x, int y) {
        return inBounds(x, y, 0);
    }

    /**
     * Returns tile at given coordinates as long as they are within bounds. If not, null is returned.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return tile at given coordinates
     */
    public Tile tileAt(int x, int y, int z) {
        return inBounds(x, y, z) ? tiles[x][y][z] : null;
    }

    /**
     * Places given tile at given coordinates as long as they are in bounds. Replaces the tile, if not null.
     *
     * @param tile tile to be placed
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public void placeTile(Tile tile, int x, int y, int z) {
        if (inBounds(x, y, z)) {
            tile.x = x;
            tile.y = y;
            tile.z = z;
            tiles[x][y][z] = tile;
        }
    }

    /**
     * Nullifies all tiles in given area as long as coordinates are in bounds.
     *
     * @param x0 lower x bound, inclusive
     * @param x1 upper x bound, exclusive
     * @param y0 lower y bound, inclusive
     * @param y1 upper y bound, exclusive
     * @param z0 lower z bound, inclusive
     * @param z1 upper z bound, exclusive
     */
    public void erase(int x0, int x1, int y0, int y1, int z0, int z1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                for (int z = z0; z < z1; z++) {
                    if (inBounds(x, y, z)) {
                        tiles[x][y][z] = null;
                    }
                }
            }
        }
    }

    /**
     * Convenience implementation of {@link #erase(int, int, int, int, int, int)}.
     * Erases all z tiles at one x and y coordinate.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void erase(int x, int y) {
        erase(x, x + 1, y, y + 1, 0, depth());
    }

    /**
     * Checks whether given area is completely free, meaning no solid tiles.
     *
     * @param x0 lower x bound, inclusive
     * @param x1 upper x bound, exclusive
     * @param y0 lower y bound, inclusive
     * @param y1 upper y bound, exclusive
     * @param z0 lower z bound, inclusive
     * @param z1 upper z bound, exclusive
     * @return whether area is free
     */
    public boolean isFree(int x0, int x1, int y0, int y1, int z0, int z1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                for (int z = z0; z < z1; z++) {
                    if (inBounds(x, y, z)) {
                        Tile t = tileAt(x, y, z);

                        if (t != null && t.solid) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Implementation of {@link #isFree(int, int, int, int, int, int)}.
     *
     * Checks whether given area specified by radius is free, meaning no solid tiles.
     * Note that despite being called radius, the area is rectangular.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param r area radius
     * @return
     */
    public boolean isFree(int x, int y, int z, int r) {
        return isFree(x - r, x + 1 + r, y - r, y + 1 + r, z, z + 1);
    }

    /** @return width of tiles */
    public int width() {
        return tiles.length;
    }

    /** @return height of tiles */
    public int height() {
        return tiles[0].length;
    }

    /** @return depth of tiles */
    public int depth() {
        return tiles[0][0].length;
    }

    /**
     * Initializes tiles with given dimensions.
     *
     * @param w width
     * @param h height
     * @param d depth
     */
    public void initSize(int w, int h, int d) {
        tiles = new Tile[w][h][d];
        solidTiles = new Rectangle[w][h];
    }
}
