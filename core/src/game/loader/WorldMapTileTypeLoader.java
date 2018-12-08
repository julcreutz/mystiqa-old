package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.world_map.tile.WorldMapTileType;

import java.util.HashMap;

public class WorldMapTileTypeLoader {
    private static HashMap<String, WorldMapTileType> types;

    public static void load() {
        types = new HashMap<String, WorldMapTileType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/world_map_tile_types.json"))) {
            WorldMapTileType type = new WorldMapTileType();

            type.name = json.getString("name");
            type.connect = json.get("connect").asStringArray();

            type.sheet = SheetLoader.load(json.getString("sheet"));
            type.color = ColorLoader.load(json.getString("color"));

            type.traversalCost = json.getFloat("traversalCost");

            types.put(json.getString("id"), type);
        }
    }

    public static WorldMapTileType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }
}
