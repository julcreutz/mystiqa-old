package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.world_map.entity.WorldMapPlayerType;

import java.util.HashMap;

public class WorldMapPlayerTypeLoader {
    private static HashMap<String, WorldMapPlayerType> types;

    public static void load() {
        types = new HashMap<String, WorldMapPlayerType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/world_map_player_types.json"))) {
            WorldMapPlayerType type = new WorldMapPlayerType();

            type.feet = SheetLoader.load(json.getString("feet"));
            type.body = SheetLoader.load(json.getString("body"));
            type.head = SheetLoader.load(json.getString("head"));

            type.animSpeed = json.getFloat("animSpeed");

            type.color = ColorLoader.load(json.getString("color"));

            types.put(json.getString("id"), type);
        }
    }

    public static WorldMapPlayerType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }
}
