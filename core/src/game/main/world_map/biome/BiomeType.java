package game.main.world_map.biome;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BiomeType {
    public String name;
    public String[] connect;

    public TextureRegion[][] sheet;
    public String[] colors;

    public float minElevation;
    public float maxElevation;

    public boolean traversable;
    public float traversalCost;

    public boolean connect(BiomeType type) {
        if (connect != null) {
            for (String _connect : connect) {
                if (_connect.equals(type.name)) {
                    return true;
                }
            }
        }

        return name.equals(type.name);
    }
}
