package mystiqa.stat;

import com.badlogic.gdx.utils.JsonValue;

public abstract class Stat {
    public abstract void deserialize(JsonValue json);
}
