package game.main.positionable.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.state.play.map.Map;

/**
 * Helper class that handles {@link Tile} work.
 */
public class TileManager {
    /** Holds reference of map. */
    public Map map;
    /** Stores tiles. */
    public Tile[][] tiles;

    /**
     * Constructs new instance with map reference.
     *
     * @param map map reference
     */
    public TileManager(Map map) {
        this.map = map;
    }

    /**
     * Updates all tiles in a given area.
     *
     * If a tile is newly created it will be updated even when the camera is moving to prevent missing textures.
     *
     * @param x0 lower x bound, inclusive
     * @param x1 upper x bound, exclusive
     * @param y0 lower y bound, inclusive
     * @param y1 upper y bound, exclusive
     */
    public void update(int x0, int x1, int y0, int y1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                Tile tile = tiles[x][y];

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

    /**
     * Renders tiles in a given area.
     *
     * @param batch sprite batch to use
     * @param x0 lower x bound, inclusive
     * @param x1 upper x bound, exclusive
     * @param y0 lower y bound, inclusive
     * @param y1 upper y bound, exclusive
     */
    public void render(SpriteBatch batch, int x0, int x1, int y0, int y1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                Tile tile = tiles[x][y];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }
    }

    /**
     * Checks whether given coordinates are within tile outerBounds.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return whether coordinates are in outerBounds
     */
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    /**
     * Returns tile at given coordinates as long as they are within outerBounds. If not, null is returned.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return tile at given coordinates
     */
    public Tile at(int x, int y) {
        return inBounds(x, y) ? tiles[x][y] : null;
    }

    /**
     * Places given tile at given coordinates as long as they are in outerBounds. Replaces the tile, if not null.
     *
     * @param tile tile to be placed
     * @param x x coordinate
     * @param y y coordinate
     */
    public void set(Tile tile, int x, int y) {
        if (inBounds(x, y)) {
            tile.x = x;
            tile.y = y;

            tiles[x][y] = tile;
        }
    }

    /**
     * Nullifies all tiles in given area as long as coordinates are in outerBounds.
     *
     * @param x0 lower x bound, inclusive
     * @param x1 upper x bound, exclusive
     * @param y0 lower y bound, inclusive
     * @param y1 upper y bound, exclusive
     */
    public void clear(int x0, int x1, int y0, int y1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                tiles[x][y] = null;
            }
        }
    }

    /**
     * Checks whether given area is completely free, meaning no solid tiles.
     *
     * @param x0 lower x bound, inclusive
     * @param x1 upper x bound, exclusive
     * @param y0 lower y bound, inclusive
     * @param y1 upper y bound, exclusive
     * @return whether area is free
     */
    public boolean isFree(int x0, int x1, int y0, int y1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                if (inBounds(x, y)) {
                    Tile t = at(x, y);

                    if (t != null && t.hitbox == null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Implementation of {@link #isFree(int, int, int, int)}.
     *
     * Checks whether given area specified by radius is free, meaning no solid tiles.
     * Note that despite being called radius, the area is rectangular.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param r area radius
     * @return whether area is free
     */
    public boolean isFree(int x, int y, int r) {
        return isFree(x - r, x + 1 + r, y - r, y + 1 + r);
    }

    /** @return width of tiles */
    public int getWidth() {
        return tiles.length;
    }

    /** @return height of tiles */
    public int getHeight() {
        return tiles[0].length;
    }

    /**
     * Initializes {@link #tiles} as new empty array with given dimensions.
     *
     * @param w width
     * @param h height
     */
    public void initSize(int w, int h) {
        tiles = new Tile[w][h];
    }
}
