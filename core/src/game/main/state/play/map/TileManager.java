package game.main.state.play.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import game.main.state.play.map.tile.Tile;
import game.main.state.play.map.tile.TileType;

public class TileManager {
    public Tile[][][] tiles;
    public Rectangle[][] solidTiles;

    public void update(Map map, int x0, int x1, int y0, int y1) {
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

    public Tile tileAt(Tile[][][] tiles, int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
    }

    public Tile tileAt(int x, int y, int z) {
        return tileAt(tiles, x, y, z);
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

    public Tile[][][] copyTiles() {
        Tile[][][] tiles = new Tile[this.tiles.length][this.tiles[0].length][this.tiles[0][0].length];

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                System.arraycopy(this.tiles[x][y], 0, tiles[x][y], 0, tiles[0][0].length);
            }
        }

        return tiles;
    }

    public void erase(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            for (int z = 0; z < tiles[0][0].length; z++) {
                tiles[x][y][z] = null;
            }
        }
    }

    public boolean isFree(Tile[][][] tiles, int x, int y, int z, int r) {
        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                Tile t = tileAt(x + xx, y + yy, z);

                if (t != null && t.type.solid) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isFree(int x, int y, int z, int r) {
        return isFree(tiles, x, y, z, r);
    }

    public int countSolid(int x, int y, int z, int r) {
        int n = 0;

        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                Tile t = tileAt(x + xx, y + yy, z);

                if (t != null && t.type.solid) {
                    n++;
                }
            }
        }

        return n;
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
