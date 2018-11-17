package mystiqa.world_generation.biome;

import mystiqa.Resources;
import mystiqa.entity.tile.Tile;

public class Hills extends Biome {
    public Hills() {
        targetElevation = .575f;

        frequency = .0075f;
        octaves = 8;

        minHeight = -16;
        maxHeight = 32;
    }

    @Override
    public Tile getGroundTile() {
        return Resources.getInstance().getTile("Hill");
    }

    @Override
    public Tile getWaterTile() {
        return Resources.getInstance().getTile("Grass");
    }
}
