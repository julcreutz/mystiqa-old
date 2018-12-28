package game.main;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class Color extends com.badlogic.gdx.graphics.Color implements Serializable {
    @Override
    public void deserialize(JsonValue json) {
        r = json.getInt("r", 255) / 255f;
        g = json.getInt("g", 255) / 255f;
        b = json.getInt("b", 255) / 255f;
        a = 1;
    }
}
