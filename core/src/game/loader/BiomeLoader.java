package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.world_map.biome.BiomeType;

import java.util.HashMap;

public class BiomeLoader {
    private static HashMap<String, BiomeType> types;

    public static void load() {
        types = new HashMap<String, BiomeType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/biomes.json"))) {
            BiomeType type = new BiomeType();

            type.name = json.getString("name");
            type.connect = json.get("connect").asStringArray();

            type.sheet = SheetLoader.load(json.getString("sheet"));
            type.colors = json.get("colors").asStringArray();

            type.minElevation = json.getFloat("minElevation");
            type.maxElevation = json.getFloat("maxElevation");

            type.traversable = json.getBoolean("traversable");
            type.traversalCost = json.getFloat("traversalCost");

            types.put(json.name, type);
        }
    }

    public static BiomeType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }

    public static Array<BiomeType> loadAll() {
        Array<BiomeType> types = new Array<BiomeType>();

        for (BiomeType type : BiomeLoader.types.values()) {
            types.add(type);
        }

        return types;
    }
}
