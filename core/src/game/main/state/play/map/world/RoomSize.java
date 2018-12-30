package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class RoomSize implements Serializable {
    public float chance;

    public int width;
    public int height;

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
    }
}
