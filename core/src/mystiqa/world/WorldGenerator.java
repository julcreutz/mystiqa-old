package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.noise.Noise;
import mystiqa.entity.tile.Tile;
import mystiqa.noise.NoiseParameters;
import mystiqa.world.biome.Biome;
import mystiqa.world.structure.StructureSave;

public class WorldGenerator {
    public int waterLevel;

    public Array<Biome> possibleBiomes;

    public Noise elevationNoise;
    public NoiseParameters elevationNoiseParams;

    public Noise temperatureNoise;
    public NoiseParameters temperatureNoiseParams;

    public Noise moistureNoise;
    public NoiseParameters moistureNoiseParams;

    public Array<StructureSave> structureSaves;
    public Array<PlaceTile> placeTiles;

    public WorldGenerator() {
        possibleBiomes = new Array<Biome>();

        elevationNoise = new Noise(0);
        elevationNoiseParams = new NoiseParameters(4, .00075f, 1);

        temperatureNoise = new Noise(1);
        temperatureNoiseParams = new NoiseParameters(2, .00075f, 1);

        moistureNoise = new Noise(2);
        moistureNoiseParams = new NoiseParameters(2, .00075f, 1);

        structureSaves = new Array<StructureSave>();
        placeTiles = new Array<PlaceTile>();
    }

    public void generateChunk(final Chunk c, int lod) {
        switch (lod) {
            case 1:
                if (c.lod == -1) {
                    c.lod = 1;

                    // Trees
                    for (int x = 0; x < c.tiles.length; x++) {
                        for (int y = 0; y < c.tiles[0].length; y++) {
                            int xx = c.x + x;
                            int yy = c.y + y;

                            Biome biome = getBiome(xx, yy);
                            int height = getHeight(xx, yy);

                            if (biome.treeDensity != null && height > waterLevel && isPeak(biome.treeDensity, xx, yy, 1)) {
                                for (int z = 0; z < MathUtils.round(MathUtils.lerp(biome.tree.minHeight, biome.tree.maxHeight, elevationNoise.get(xx, yy, biome.treeHeight))); z++) {
                                    PlaceTile placeTile = new PlaceTile();
                                    placeTile.tile = "Log";
                                    placeTile.x = xx;
                                    placeTile.y = yy;
                                    placeTile.z = height + 1 + z;
                                    placeTiles.add(placeTile);
                                }
                            }
                        }
                    }
                }

                break;
            case 0:
                if (c.lod == 1) {
                    c.lod = 0;

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

                break;
        }
    }

    public Tile get(int x, int y, int z) {
        for (PlaceTile placeTile : placeTiles) {
            if (x == placeTile.x && y == placeTile.y && z == placeTile.z) {
                placeTiles.removeValue(placeTile, true);

                return Assets.getTile(placeTile.tile);
            }
        }

        Biome biome = getBiome(x, y);
        int height = getHeight(x, y);

        if (z <= waterLevel) {
            if (z <= height) {
                return Assets.getTile(biome.underWaterTile);
            } else {
                return Assets.getTile(biome.waterTile);
            }
        } else {
            if (z <= height) {
                return Assets.getTile(biome.aboveWaterTile);
            }
        }

        return null;
    }

    public int getHeight(int x, int y) {
        float[] weights = new float[possibleBiomes.size];

        float min = 1;
        float max = 0;

        for (int i = 0; i < possibleBiomes.size; i++) {
            weights[i] = evaluateBiome(possibleBiomes.get(i),
                    elevationNoise.get(x, y, elevationNoiseParams),
                    temperatureNoise.get(x, y, temperatureNoiseParams),
                    moistureNoise.get(x, y, moistureNoiseParams));

            if (weights[i] < min) {
                min = weights[i];
            }

            if (weights[i] > max) {
                max = weights[i];
            }
        }

        for (int i = 0; i < weights.length; i++) {
            weights[i] = (weights[i] - min) / (max - min);
        }

        float totalHeight = 0;

        for (int i = 0; i < possibleBiomes.size; i++) {
            totalHeight += possibleBiomes.get(i).getHeight(elevationNoise, x, y) * weights[i];
        }

        return (int) (waterLevel + totalHeight);
    }

    public Biome getBiome(int x, int y) {
        float elev = elevationNoise.get(x, y, elevationNoiseParams);
        float temp = temperatureNoise.get(x, y, temperatureNoiseParams);
        float moist = moistureNoise.get(x, y, moistureNoiseParams);

        Biome b = null;
        float best = 0;

        for (Biome _b : possibleBiomes) {
            float eval = evaluateBiome(_b, elev, temp, moist);

            if (eval >= best) {
                b = _b;
                best = eval;
            }
        }

        return b;
    }

    public float evaluateBiome(Biome b, float elevation, float temperature, float moisture) {
        float _elevation = 1 - Math.abs(b.targetElevation - elevation);
        float _temperature = 1 - Math.abs(b.targetTemperature - temperature);
        float _moisture = 1 - Math.abs(b.targetMoisture - moisture);

        return _elevation * _temperature * _moisture;
    }

    public boolean isPeak(NoiseParameters params, int x, int y, int r) {
        float curr = elevationNoise.get(x, y, params);

        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                if ((xx != 0 || yy != 0) && elevationNoise.get(x + xx, y + yy, params) >= curr) {
                    return false;
                }
            }
        }

        return true;
    }
}
