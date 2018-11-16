package mystiqa.world_generation.biome;

import mystiqa.Resources;
import mystiqa.entity.tile.Tile;

public class Grassland extends Biome {
    @Override
    public float getFrequency() {
        return 0.0075f;
    }

    @Override
    public int getOctaves() {
        return 8;
    }

    @Override
    public float getMultiplier() {
        return 4;
    }

    @Override
    public float getHeight() {
        return 16f;
    }

    @Override
    public int getHeightOffset() {
        return 56;
    }

    @Override
    public Tile getGroundTile() {
        return Resources.getTile("Grass");
    }

    @Override
    public Tile getWaterTile() {
        return Resources.getTile("Grass");
    }
}
