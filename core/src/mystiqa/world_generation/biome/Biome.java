package mystiqa.world_generation.biome;

import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;

public abstract class Biome {
    private Perlin perlin;

    public Biome() {
        perlin = new Perlin();
    }

    public int getHeight(int x, int y) {
        return getHeightOffset() + (int) (perlin.layeredNoise(x, y, getOctaves(), getFrequency(), getMultiplier(), 1, 1f / getMultiplier()) * getHeight());
    }

    public abstract float getFrequency();
    public abstract int getOctaves();
    public abstract float getMultiplier();
    public abstract float getHeight();
    public abstract int getHeightOffset();

    public abstract Tile getGroundTile();
    public abstract Tile getWaterTile();
}
