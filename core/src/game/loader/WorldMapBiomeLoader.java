package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.world_map.biome.WorldMapBiome;
import game.main.world_map.biome.WorldMapBiomeTile;

import java.util.HashMap;

public class WorldMapBiomeLoader {
    private static HashMap<String, WorldMapBiome> biomes;

    public static void load() {
        biomes = new HashMap<String, WorldMapBiome>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/world_map_biomes.json"))) {
            WorldMapBiome biome = new WorldMapBiome();

            for (JsonValue tile : json.get("tiles")) {
                WorldMapBiomeTile _tile = new WorldMapBiomeTile();

                _tile.minElevation = tile.getFloat("minElevation");
                _tile.maxElevation = tile.getFloat("maxElevation");

                _tile.riverDensity = tile.getFloat("riverDensity");

                _tile.type = WorldMapTileTypeLoader.load(tile.getString("type"));

                biome.tiles.add(_tile);
            }

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
