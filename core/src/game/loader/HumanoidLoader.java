package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.play.entity.humanoid.HumanoidType;

import java.util.HashMap;

public class HumanoidLoader {
    private static HashMap<String, HumanoidType> humanoids;

    public static void load() {
        humanoids = new HashMap<String, HumanoidType>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/humanoids.json"))) {
            HumanoidType humanoid = new HumanoidType();
            humanoid.deserialize(json);

            humanoids.put(json.name, humanoid);
        }
    }

    public static HumanoidType load(String name) {
        return humanoids.containsKey(name) ? humanoids.get(name) : null;
    }
}
