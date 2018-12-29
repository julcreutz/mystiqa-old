package game.main.stat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class StatManager implements Serializable {
    public Array<Stat> stats;

    public StatManager() {
        stats = new Array<>();
    }

    public float count(Stat.Type type) {
        float absolute = 0;
        float relative = 1;

        for (Stat s : stats) {
            if (s.type == type) {
                absolute += s.absolute;
                relative *= s.relative;
            }
        }

        return absolute * relative;
    }

    public boolean has(Stat.Type type) {
        for (Stat s : stats) {
            if (s.type == type) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deserialize(JsonValue json) {
        for (JsonValue stat : json) {
            Stat s = new Stat();
            s.deserialize(stat);
            stats.add(s);
        }
    }
}