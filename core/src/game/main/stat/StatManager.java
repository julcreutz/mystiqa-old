package game.main.stat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class StatManager implements StatCounter {
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

        return value * (MathUtils.clamp(1 + multiplier, 0, Float.MAX_VALUE));
    }

    @Override
    public StatManager getStats() {
        return this;
    }
}
