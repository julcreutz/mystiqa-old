package mystiqa.world_generation.biome;

import mystiqa.Resources;
import mystiqa.entity.tile.Tile;

public class Grassland extends Biome {
    public Grassland() {
        targetElevation = .3f;

        frequency = .0075f * .5f;
        octaves = 4;
        persistence = 1;

        minHeight = -8;
        maxHeight = 8;
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
