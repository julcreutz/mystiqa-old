package game.loader.resource.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class ColorResource extends Color implements Serializable {
    @Override
    public void deserialize(JsonValue json) {
        r = json.getInt("r", 255) / 255f;
        g = json.getInt("g", 255) / 255f;
        b = json.getInt("b", 255) / 255f;
        a = 1;
    }
}
