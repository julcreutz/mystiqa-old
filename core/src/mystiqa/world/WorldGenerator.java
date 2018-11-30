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

    public Noise noise;

    public NoiseParameters elevationNoise;
    public NoiseParameters temperatureNoise;
    public NoiseParameters moistureNoise;

    public Array<StructureSave> structureSaves;
    public Array<PlaceTile> placeTiles;

    public WorldGenerator() {
        possibleBiomes = new Array<Biome>();

        noise = new Noise(0);

        elevationNoise = new NoiseParameters(4, .0075f, 1);
        temperatureNoise = new NoiseParameters(2, .00375f, 1);
        moistureNoise = new NoiseParameters(2, .00375f, 1);

        structureSaves = new Array<StructureSave>();
        placeTiles = new Array<PlaceTile>();
    }

    public void generateChunk(Chunk c, int lod) {
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

                            if (height > waterLevel && isPeak(biome.treeDensity, xx, yy, 1)) {
                                for (int z = 0; z < MathUtils.round(MathUtils.lerp(biome.tree.minHeight, biome.tree.maxHeight, noise.get(xx, yy, biome.treeHeight))); z++) {
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
        return waterLevel + getBiome(x, y).getHeight(noise, x, y);
    }

    public Biome getBiome(int x, int y) {
        float elev = noise.get(x, y, elevationNoise);
        float temp = noise.get(x, y, temperatureNoise);
        float moist = noise.get(x, y, moistureNoise);

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
        float curr = noise.get(x, y, params);

        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                if ((xx != 0 || yy != 0) && noise.get(x + xx, y + yy, params) >= curr) {
                    return false;
                }
            }
        }

        return true;
    }

    public long seed() {
        return 0;
    }
}
