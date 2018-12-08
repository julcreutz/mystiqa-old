package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.world_map.WorldMapEntityType;

import java.util.HashMap;

public class WorldMapEntityTypeLoader {
    private static HashMap<String, WorldMapEntityType> types;

    public static void load() {
        types = new HashMap<String, WorldMapEntityType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/world_map_entity_types.json"))) {
            WorldMapEntityType type = new WorldMapEntityType();

            type.sheet = SheetLoader.load(json.getString("sheet"));
            type.animSpeed = json.getFloat("animSpeed");

            types.put(json.getString("id"), type);
        }
    }

    public static WorldMapEntityType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }
}
