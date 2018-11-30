package mystiqa.world;

import com.badlogic.gdx.utils.JsonValue;

public class Tree {
    public int minHeight;
    public int maxHeight;

    public void deserialize(JsonValue json) {
        if (json.has("minHeight")) {
            minHeight = json.getInt("minHeight");
        }

        if (json.has("maxHeight")) {
            maxHeight = json.getInt("maxHeight");
        }
    }
}
