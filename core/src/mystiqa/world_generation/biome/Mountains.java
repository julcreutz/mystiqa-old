package mystiqa.world_generation.biome;

import mystiqa.Resources;
import mystiqa.entity.tile.Tile;

public class Mountains extends Biome {
    public Mountains() {
        targetElevation = .7f;

        frequency = .0075f * 4f;
        octaves = 4;
        persistence = 1;

        minHeight = -32;
        maxHeight = 64;
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
