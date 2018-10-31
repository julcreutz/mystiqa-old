package mystiqa.item;

import com.badlogic.gdx.utils.JsonValue;

public abstract class Item {
    public String name;

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }
    }
}
