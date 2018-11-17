package mystiqa.world_generation.biome;

import com.badlogic.gdx.math.MathUtils;
import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;

public abstract class Biome {
    public float targetElevation;
    //public float targetTemperature;

    public float frequency;
    public int octaves;
    public float persistence;

    public int minHeight;
    public int maxHeight;

    private Perlin perlin;

    public Biome() {
        perlin = new Perlin();
    }

    public int getHeight(int x, int y) {
        return (int) MathUtils.lerp(minHeight, maxHeight, perlin.layeredNoise(x, y, frequency, octaves, persistence));
    }

    public abstract Tile getGroundTile();
    public abstract Tile getWaterTile();
}
