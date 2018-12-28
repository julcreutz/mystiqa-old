package game.main.gen;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class RoomTemplate implements Serializable {
    public float chance;

    public int width;
    public int height;

    public char[][] layout;

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("chance")) {
            chance = json.getFloat("chance");
        }

        if (json.has("width")) {
            width = json.getInt("width");
        }

        if (json.has("height")) {
            height = json.getInt("height");
        }

        if (json.has("layout")) {
            layout = new char[height * 4][width * 8];

            int i = 0;
            for (JsonValue row : json.get("layout")) {
                layout[i] = row.asCharArray();
                i++;
            }
        }
    }
}
