package game.main.world_map.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldMapTileType {
    public String name;
    public String[] connect;

    public TextureRegion[][] sheet;
    public String[] colors;

    public boolean traversable;
    public float traversalCost;

    public boolean connect(WorldMapTileType type) {
        for (String _connect : connect) {
            if (_connect.equals(type.name)) {
                return true;
            }
        }

        return name.equals(type.name);
    }
}
