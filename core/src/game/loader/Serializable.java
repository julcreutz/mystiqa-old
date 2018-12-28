package game.loader;

import com.badlogic.gdx.utils.JsonValue;

public interface Serializable {
    void deserialize(JsonValue json);
}
