package mystiqa.world_generation.biome;

import mystiqa.entity.tile.Grass;
import mystiqa.entity.tile.Hill;
import mystiqa.entity.tile.Tile;
import mystiqa.entity.tile.Water;

public class Hills extends Biome {
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
        return 32f;
    }

    @Override
    public int getHeightOffset() {
        return 56;
    }

    @Override
    public Tile getGroundTile() {
        return new Hill();
    }

    @Override
    public Tile getWaterTile() {
        return new Grass();
    }
}
