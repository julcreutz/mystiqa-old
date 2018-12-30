package game.main.stat;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class Stat implements Serializable {
    public enum Type {
        HEALTH,
        SPEED,
        PHYSICAL_DAMAGE
    }

    public Type type;

    public float absolute;
    public float relative;

    public Stat(Type type, float absolute, float relative) {
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
            type = Type.valueOf(json.getString("type"));
        }

        if (json.has("absolute")) {
            absolute = json.getFloat("absolute");
        }

        if (json.has("relative")) {
            relative = json.getFloat("relative");
        }
    }
}
