package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.play.entity.Entity;
import game.main.play.entity.humanoid.Humanoid;
import game.main.play.entity.slime.Slime;

import java.util.HashMap;

public class EntityLoader {
    private static HashMap<String, JsonValue> entities;

    public static void load() {
        entities = new HashMap<>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/entities.json"))) {
            entities.put(json.name, json.child);
        }
    }

    public static Entity load(String name) {
        if (entities.containsKey(name)) {
            JsonValue json = entities.get(name);

            String _name = json.name;
            Entity e = null;

            if (_name.equals("Humanoid")) {
                e = new Humanoid();
            } else if (_name.equals("Slime")) {
                e = new Slime();
            }

            if (e != null) {
                e.deserialize(json);
                return e;
            }
        }

        return null;
    }
}
