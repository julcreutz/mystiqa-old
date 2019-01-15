package game.main.stat;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class Stat implements Serializable {
    public enum Type {
        HEALTH,
        SPEED,
        MIN_PHYSICAL_DAMAGE,
        MAX_PHYSICAL_DAMAGE
    }

    public Type type;

    public float value;
    public float multiplier;

    public Stat(Type type, float value, float multiplier) {
        this.type = type;
        this.value = value;
        this.multiplier = multiplier;
    }

    public Stat(Type type) {
        this(type, 0, 0);
    }

    public Stat() {
        this(null, 0, 0);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue type = json.get("type");
        if (type != null) {
            this.type = Stat.Type.valueOf(type.asString());
        }

        JsonValue absolute = json.get("value");
        if (absolute != null) {
            this.value = absolute.asFloat();
        }

        JsonValue relative = json.get("multiplier");
        if (relative != null) {
            this.multiplier = relative.asFloat();
        }
    }
}
