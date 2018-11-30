package mystiqa.world;

import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;
import mystiqa.world.biome.Biome;
import mystiqa.world.structure.StructureSave;

public class WorldGenerator {
    public int waterLevel;

    public Array<Biome> possibleBiomes;

    public Perlin elevationNoise;
    public Perlin temperatureNoise;
    public Perlin moistureNoise;

    public Array<StructureSave> structureSaves;
    public Array<PlaceTile> placeTiles;

    public WorldGenerator() {
        possibleBiomes = new Array<Biome>();

        elevationNoise = new Perlin(.0075f, 4, 1, seed());
        temperatureNoise = new Perlin(.00375f, 2, 1, seed());
        moistureNoise = new Perlin(.00375f, 2, 1, seed());

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

                            main: if (height > waterLevel) {
                                // Find peak
                                float curr = biome.treeNoise.get(xx, yy);

                                for (int _x = -1; _x <= 1; _x++) {
                                    for (int _y = -1; _y <= 1; _y++) {
                                        if (biome.treeNoise.get(xx + _x, yy + _y) > curr) {
                                            break main;
                                        }
                                    }
                                }

                                PlaceTile placeTile = new PlaceTile();
                                placeTile.tile = "Log";
                                placeTile.x = xx;
                                placeTile.y = yy;
                                placeTile.z = height + 1;
                                placeTiles.add(placeTile);
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
        return waterLevel + getBiome(x, y).getHeight(x, y);
    }

    public Biome getBiome(int x, int y) {
        float elev = elevationNoise.get(x, y);
        float temp = temperatureNoise.get(x, y);
        float moist = moistureNoise.get(x, y);

        Biome b = null;
        float best = 0;

        for (Biome _b : possibleBiomes) {
            float eval = getBiomeEvaluation(_b, elev, temp, moist);

            if (eval >= best) {
                b = _b;
                best = eval;
            }
        }

        return b;
    }

    public float getBiomeEvaluation(Biome b, float elevation, float temperature, float moisture) {
        float _elevation = 1 - Math.abs(b.targetElevation - elevation);
        float _temperature = 1 - Math.abs(b.targetTemperature - temperature);
        float _moisture = 1 - Math.abs(b.targetMoisture - moisture);

        return _elevation * _temperature * _moisture;
    }

    public long seed() {
        return 0;
    }
}
