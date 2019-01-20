package game.main.object;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class GameObject implements Serializable {
    public String id;

    @Override
    public void deserialize(JsonValue json) {
        id = json.parent.name;
    }
}
