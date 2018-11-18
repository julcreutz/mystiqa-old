package mystiqa.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Perlin;
import mystiqa.Assets;
import mystiqa.entity.tile.Chunk;
import mystiqa.entity.tile.Tile;

import java.util.HashMap;
import java.util.Vector;

public class WorldGenerator {
    public int waterLevel;

    private Array<Biome> biomes;
    private Array<Terrain> terrain;

    private Perlin elevationNoise;
    private Perlin temperatureNoise;
    private Perlin moistureNoise;

    public WorldGenerator() {
        waterLevel = 0;

        biomes = new Array<Biome>();
        terrain = new Array<Terrain>();

        elevationNoise = new Perlin();
        temperatureNoise = new Perlin();
        moistureNoise = new Perlin();
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
        Biome b = getBiome(x, y);
        int height = getHeight(x, y);

        if (height < waterLevel) {
            if (z > height && z <= waterLevel) {
                return Assets.getInstance().getTile(b.waterTile);
            } else if (z <= height) {
                return Assets.getInstance().getTile(b.underWaterTile);
            }
        } else {
            if (z <= height) {
                return Assets.getInstance().getTile(b.aboveWaterTile);
            }
        }

        return null;
    }

    public float getElevation(int x, int y) {
        return elevationNoise.layeredNoise(x, y, .0075f, 4, 1);
    }

    public float getTemperature(int x, int y) {
        return temperatureNoise.layeredNoise(x, y, .00375f, 2, 1);
    }

    public float getMoisture(int x, int y) {
        return moistureNoise.layeredNoise(x, y, .00375f, 4, 1);
    }

    public int getHeight(int x, int y) {
        return waterLevel + getTerrain(x, y).getHeight(x, y) + getBiome(x, y).heightOffset;
    }

    public HashMap<Terrain, Float> getPossibleTerrain(int x, int y) {
        Biome b = getBiome(x, y);

        HashMap<Terrain, Float> terrain = new HashMap<Terrain, Float>();

        float elevation = getElevation(x, y);

        for (Terrain t : this.terrain) {
            if (b.terrain.contains(t, true)) {
                terrain.put(t, 1 - Math.abs(t.targetElevation - elevation));
            }
        }

        return terrain;
    }

    public HashMap<Biome, Vector2> getPossibleBiomes(int x, int y) {
        HashMap<Biome, Vector2> biomes = new HashMap<Biome, Vector2>();

        float temperature = getTemperature(x, y);
        float moisture = getMoisture(x, y);

        for (Biome b : this.biomes) {
            biomes.put(b, new Vector2(1 - Math.abs(b.targetTemperature - temperature), 1 - Math.abs(b.targetMoisture - moisture)));
        }

        return biomes;
    }

    public Terrain getTerrain(int x, int y) {
        HashMap<Terrain, Float> terrain = getPossibleTerrain(x, y);

        Terrain result = null;
        float max = 0;

        for (Terrain t : terrain.keySet()) {
            float curr = terrain.get(t);

            if (curr > max) {
                max = curr;
                result = t;
            }
        }

        return result;
    }

    public Biome getBiome(int x, int y) {
        HashMap<Biome, Vector2> biomes = getPossibleBiomes(x, y);

        Biome result = null;
        float max = 0;

        for (Biome b : biomes.keySet()) {
            float curr = biomes.get(b).x * biomes.get(b).y;

            if (curr >= max) {
                max = curr;
                result = b;
            }
        }

        return result;
    }

    public void deserialize(JsonValue json) {
        if (json.has("waterLevel")) {
            waterLevel = json.getInt("waterLevel");
        }

        if (json.has("biomes")) {
            for (JsonValue biome : json.get("biomes")) {
                biomes.add(Assets.getInstance().getBiome(biome.asString()));
            }
        }

        if (json.has("terrain")) {
            for (JsonValue terrain : json.get("terrain")) {
                this.terrain.add(Assets.getInstance().getTerrain(terrain.asString()));
            }
        }
    }
}
