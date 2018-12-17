package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.site.tile.TileType;

import java.util.HashMap;

public class SiteTileTypeLoader {
    private static HashMap<String, TileType> types;

    public static void load() {
        types = new HashMap<String, TileType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/tiles.json"))) {
            TileType type = new TileType();

            if (json.has("name")) {
                type.name = json.getString("name");
            }

            if (json.has("sheet")) {
                type.sheet = SheetLoader.load(json.getString("sheet"));
            }

            if (json.has("colors")) {
                type.colors = json.get("colors").asStringArray();
            }

            if (json.has("autoTile")) {
                type.autoTile = json.getBoolean("autoTile");
            }

            if (json.has("connect")) {
                type.connect = json.get("connect").asStringArray();
            }

            types.put(json.name, type);
        }
    }

    public static TileType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }
}
