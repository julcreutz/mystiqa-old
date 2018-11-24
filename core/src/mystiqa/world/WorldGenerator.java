package mystiqa.world;

import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;

public class WorldGenerator {
    public int waterLevel;

    public Array<Biome> possibleBiomes;

    public Perlin elevationNoise;
    public Perlin temperatureNoise;
    public Perlin moistureNoise;

    public WorldGenerator() {
        possibleBiomes = new Array<Biome>();

        elevationNoise = new Perlin(.0075f, 4);
        temperatureNoise = new Perlin(.00375f, 2);
        moistureNoise = new Perlin(.00375f, 2);
    }

    public void generateChunk(Chunk c) {
        for (int x = 0; x < c.tiles.length; x++) {
            for (int y = 0; y < c.tiles[0].length; y++) {
                for (int z = 0; z < c.tiles[0][0].length; z++) {
                    Tile t = get(c.x + x, c.y + y, c.z + z);

                    if (t != null) {
                        c.setTile(t, x, y, z);
                    }
                }
            }
        }
    }

    public Tile get(int x, int y, int z) {
        Biome biome = getBiome(x, y);
        int height = getHeight(x, y);

        if (z <= waterLevel) {
            if (z <= height) {
                return Assets.getInstance().getTile(biome.underWaterTile);
            } else {
                return Assets.getInstance().getTile(biome.waterTile);
            }
        } else {
            if (z <= height) {
                return Assets.getInstance().getTile(biome.aboveWaterTile);
            }
        }

        return null;
    }

    public int getHeight(int x, int y) {
        return waterLevel + getBiome(x, y).getHeight(x, y);
    }

    public Biome getBiome(int x, int y) {
        float elevation = elevationNoise.get(x, y);
        float temperature = temperatureNoise.get(x, y);
        float moisture = moistureNoise.get(x, y);

        Biome biome = null;
        float best = 0;

        for (Biome _biome : possibleBiomes) {
            float _elevation = 1 - Math.abs(_biome.targetElevation - elevation);
            float _temperature = 1 - Math.abs(_biome.targetTemperature - temperature);
            float _moisture = 1 - Math.abs(_biome.targetMoisture - moisture);

            float result = _elevation * _temperature * _moisture;

            if (result >= best) {
                biome = _biome;
                best = result;
            }
        }

        return biome;
    }
}
