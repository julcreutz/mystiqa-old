package mystiqa.world_generation.biome;

import mystiqa.Resources;
import mystiqa.entity.tile.Tile;

public class Grassland extends Biome {
    public Grassland() {
        targetElevation = .45f;

        frequency = .0075f;
        octaves = 8;

        minHeight = -8;
        maxHeight = 16;
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
