package game.main.world_map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldMapTileType {
    public String name;
    public String[] connect;

    public TextureRegion[][] sheet;
    public Color color;

    public boolean connect(WorldMapTileType type) {
        for (String _connect : connect) {
            if (_connect.equals(type.name)) {
                return true;
            }
        }

        return name.equals(type.name);
    }
}
