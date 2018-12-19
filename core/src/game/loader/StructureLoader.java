package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.play.structure.Structure;
import game.main.play.structure.Tree;

import java.util.HashMap;

public class StructureLoader {
    private static HashMap<String, Structure> structures;

    public static void load() {
        structures = new HashMap<String, Structure>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/structures.json"))) {
            Structure struct = null;

            String type = json.child.name;

            if (type.equals("Tree")) {
                struct = new Tree();
            }

            if (struct != null) {
                struct.deserialize(json.child);
                structures.put(json.name, struct);
            }
        }
    }

    public static Structure load(String name) {
        return structures.containsKey(name) ? structures.get(name) : null;
    }
}
