package game.main.stat;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class Stat implements Serializable {
    public StatType type;

    public float absolute;
    public float relative;

    public Stat(StatType type, float absolute, float relative) {
        this.type = type;
        this.absolute = absolute;
        this.relative = relative;
    }

    public Stat() {
        this(null, 0, 1);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("type")) {
            type = StatType.valueOf(json.getString("type"));
        }

        if (json.has("absolute")) {
            absolute = json.getFloat("absolute");
        }

        if (json.has("relative")) {
            relative = json.getFloat("relative");
        }
    }
}
