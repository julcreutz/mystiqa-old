package game.main.world_map;

public class WorldMapNode {
    public WorldMapNode parent;

    public int x;
    public int y;

    public float f;
    public float g;

    public WorldMapNode(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
