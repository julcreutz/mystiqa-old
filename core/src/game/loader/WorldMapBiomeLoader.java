package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.world_map.biome.WorldMapBiome;

import java.util.HashMap;

public class WorldMapBiomeLoader {
    private static HashMap<String, WorldMapBiome> biomes;

    public static void load() {
        biomes = new HashMap<String, WorldMapBiome>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/world_map_biomes.json"))) {
            WorldMapBiome biome = new WorldMapBiome();

            biome.type = WorldMapTileTypeLoader.load(json.getString("type"));

            biome.minElevation = json.getFloat("minElevation");
            biome.maxElevation = json.getFloat("maxElevation");

            biomes.put(json.getString("id"), biome);
        }
    }

    public static WorldMapBiome load(String id) {
        return biomes.containsKey(id) ? biomes.get(id) : null;
    }

    public static Array<WorldMapBiome> loadAll() {
        Array<WorldMapBiome> biomes = new Array<WorldMapBiome>();

        for (WorldMapBiome biome : WorldMapBiomeLoader.biomes.values()) {
            biomes.add(biome);
        }

        return biomes;
    }
}
