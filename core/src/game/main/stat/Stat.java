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
        JsonValue type = json.get("type");
        if (type != null) {
            this.type = StatType.valueOf(type.asString());
        }

        JsonValue absolute = json.get("absolute");
        if (absolute != null) {
            this.absolute = absolute.asFloat();
        }

        JsonValue relative = json.get("relative");
        if (relative != null) {
            this.relative = relative.asFloat();
        }
    }
}
