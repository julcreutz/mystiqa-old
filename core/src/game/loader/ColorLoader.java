package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class ColorLoader {
    private static HashMap<String, Color> colors;

    public static void load() {
        colors = new HashMap<String, Color>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/colors.json"))) {
            Color c = new Color();

            c.r = json.getInt("r", 255) / 255f;
            c.g = json.getInt("g", 255) / 255f;
            c.b = json.getInt("b", 255) / 255f;
            c.a = 1;

            colors.put(json.name, c);
        }
    }

    public static Color load(String id) {
        return colors.containsKey(id) ? colors.get(id) : null;
    }
}
