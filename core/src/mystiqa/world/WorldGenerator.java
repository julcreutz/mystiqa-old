package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;
import mystiqa.world.biome.Biome;
import mystiqa.world.biome.Vegetation;
import mystiqa.world.structure.Structure;
import mystiqa.world.structure.StructureComponent;

public class WorldGenerator {
    public int waterLevel;

    public Array<Biome> possibleBiomes;

    public Perlin elevationNoise;
    public Perlin temperatureNoise;
    public Perlin moistureNoise;

    public Array<PlaceTile> placeTiles;

    public WorldGenerator() {
        possibleBiomes = new Array<Biome>();

        elevationNoise = new Perlin(.0075f, 4, 1, seed());
        temperatureNoise = new Perlin(.00375f, 2, 1, seed());
        moistureNoise = new Perlin(.00375f, 2, 1, seed());

        placeTiles = new Array<PlaceTile>();
    }

    public void generateChunk(Chunk c) {
        // Vegetation
        for (int x = 0; x < c.tiles.length; x++) {
            for (int y = 0; y < c.tiles[0].length; y++) {
                Biome biome = getBiome(c.x + x, c.y + y);
                int height = getHeight(c.x + x, c.y + y);

                if (height > waterLevel) {
                    for (Vegetation vegetation : biome.vegetations) {
                        if (c.getRandom().nextFloat() <= vegetation.chance) {
                            Structure structure = Assets.getInstance().getStructure(vegetation.structure);

                            for (StructureComponent component : structure.components) {
                                PlaceTile placeTile = new PlaceTile();

                                placeTile.x = c.x + x + component.x;
                                placeTile.y = c.y + y + component.y;
                                placeTile.z = height + 1 + component.z;

                                placeTile.tile = component.tile;

                                placeTiles.add(placeTile);
                            }
                        }
                    }
                }
            }
        }

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
        for (PlaceTile placeTile : placeTiles) {
            if (x == placeTile.x && y == placeTile.y && z == placeTile.z) {
                placeTiles.removeValue(placeTile, true);

                return Assets.getInstance().getTile(placeTile.tile);
            }
        }

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

    public long seed() {
        return 0;
    }
}
