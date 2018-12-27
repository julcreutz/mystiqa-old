package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.play.tile.TileType;

import java.util.HashMap;

public class TileLoader {
    private static HashMap<String, TileType> types;

    public static void load() {
        types = new HashMap<>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/tiles.json"))) {
            TileType type = new TileType();
            type.deserialize(json);

            types.put(json.name, type);
        }
    }

    public static TileType load(String id) {
        return types.getOrDefault(id, null);
    }
}
