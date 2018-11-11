package mystiqa.entity.tile;

public class Chunk {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    public static final int DEPTH = 256;

    public int x;
    public int y;
    public int z;

    public Tile[][][] tiles;

    public Chunk(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        tiles = new Tile[WIDTH][HEIGHT][DEPTH];
    }
}
