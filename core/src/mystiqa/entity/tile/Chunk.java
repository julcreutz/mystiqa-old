package mystiqa.entity.tile;

public class Chunk {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    public static final int DEPTH = 256;

    public int x;
    public int y;
    public int z;

    public Tile[][][] tiles;

    public Chunk() {
        tiles = new Tile[WIDTH][HEIGHT][DEPTH];
    }

    public boolean inBounds(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length;
    }

    public Tile getTile(int x, int y, int z) {
        return inBounds(x, y, z) ? tiles[x][y][z] : null;
    }

    public void setTile(Tile t, int x, int y, int z) {
        if (inBounds(x, y, z)) {
            t.x = (this.x + x) * 8;
            t.y = (this.y + y) * 8;
            t.z = (this.z + z) * 8;

            tiles[x][y][z] = t;
        }
    }
}
