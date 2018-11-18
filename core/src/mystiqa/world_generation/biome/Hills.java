package mystiqa.world_generation.biome;

import mystiqa.Resources;
import mystiqa.entity.tile.Tile;

public class Hills extends Biome {
    public Hills() {
        targetElevation = .5f;

        frequency = .0075f * 2f;
        octaves = 4;
        persistence = 1;

        minHeight = -32;
        maxHeight = 32;
    }

    @Override
    public Tile getGroundTile() {
        return Resources.getInstance().getTile("Grass");
    }

    @Override
    public Tile getWaterTile() {
        return Resources.getInstance().getTile("Grass");
    }
}
