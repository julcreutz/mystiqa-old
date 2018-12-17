package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.site.tile.SiteTileType;

import java.util.HashMap;

public class SiteTileTypeLoader {
    private static HashMap<String, SiteTileType> types;

    public static void load() {
        types = new HashMap<String, SiteTileType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/site_tile_types.json"))) {
            SiteTileType type = new SiteTileType();

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

            types.put(json.getString("id"), type);
        }
    }

    public static SiteTileType load(String id) {
        return types.containsKey(id) ? types.get(id) : null;
    }
}
