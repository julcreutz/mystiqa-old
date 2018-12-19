package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.region.tile.TileType;

import java.util.HashMap;

public class TileLoader {
    private static HashMap<String, TileType> types;

    public static void load() {
        types = new HashMap<String, TileType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/tiles.json"))) {
            TileType type = new TileType();

            type.name = json.getString("name", "");

            if (json.has("sheet")) {
                type.sheet = SheetLoader.load(json.getString("sheet"));
            }

            if (json.has("colors")) {
                type.colors = json.get("colors").asStringArray();
            }

            type.autoTile = json.getBoolean("autoTile", false);

            if (json.has("connect")) {
                type.connect = json.get("connect").asStringArray();
            }

            type.solid = json.getBoolean("solid", false);

            types.put(json.name, type);
        }
    }

    public static TileType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }
}
