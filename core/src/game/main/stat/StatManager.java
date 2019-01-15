package game.main.stat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class StatManager implements StatCounter, Serializable {
    public Array<Stat> stats;

    public StatManager() {
        stats = new Array<Stat>();
    }

    @Override
    public float count(Stat.Type type) {
        float value = 0;
        float multiplier = 0;

        for (Stat s : stats) {
            if (s.type == type) {
                value += s.value;
                multiplier += s.multiplier;
            }
        }

        return value * (1 + multiplier);
    }

    @Override
    public StatManager getStats() {
        return this;
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
