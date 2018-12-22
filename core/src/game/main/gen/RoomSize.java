package game.main.gen;

import com.badlogic.gdx.utils.JsonValue;

public class RoomSize {
    public float chance;

    public int width;
    public int height;

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
    }
}
