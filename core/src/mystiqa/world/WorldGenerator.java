package mystiqa.world;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;
import mystiqa.world.biome.Biome;
import mystiqa.world.biome.Vegetation;
import mystiqa.world.structure.Structure;
import mystiqa.world.structure.StructureComponent;
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

                    // Vegetation
                    for (int x = 0; x < c.tiles.length; x++) {
                        for (int y = 0; y < c.tiles[0].length; y++) {
                            int xx = c.x + x;
                            int yy = c.y + y;

                            Biome biome = getBiome(xx, yy);
                            int height = getHeight(xx, yy);

                            if (height > waterLevel) {
                                main: for (Vegetation vegetation : biome.vegetations) {
                                    for (StructureSave save : structureSaves) {
                                        if (save.structure.equals(vegetation.structure)) {
                                            float dist = new Vector3(xx, yy, height).sub(save.x, save.y, save.z).len();

                                            if (dist < vegetation.minDist + 1) {
                                                continue main;
                                            }
                                        }
                                    }

                                    if (c.getRandom().nextFloat() <= vegetation.chance) {
                                        Structure structure = Assets.getStructure(vegetation.structure);
                                        structureSaves.add(new StructureSave(vegetation.structure, xx, yy, height));

                                        for (StructureComponent component : structure.components) {
                                            PlaceTile placeTile = new PlaceTile();

                                            placeTile.x = xx + component.x;
                                            placeTile.y = yy + component.y;
                                            placeTile.z = height + 1 + component.z;

                                            placeTile.tile = component.tile;

                                            placeTiles.add(placeTile);
                                        }
                                    }
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

    public long seed() {
        return 0;
    }
}
