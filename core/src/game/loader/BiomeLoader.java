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

            type.name = json.getString("name", "");

            if (json.has("connect")) {
                type.connect = json.get("connect").asStringArray();
            }

            if (json.has("sheet")) {
                type.sheet = SheetLoader.load(json.getString("sheet"));
            }

            if (json.has("colors")) {
                type.colors = json.get("colors").asStringArray();
            }

            type.minElevation = json.getFloat("minElevation", 0);
            type.maxElevation = json.getFloat("maxElevation", 0);

            type.traversable = json.getBoolean("traversable", true);
            type.traversalCost = json.getFloat("traversalCost", 1);

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
