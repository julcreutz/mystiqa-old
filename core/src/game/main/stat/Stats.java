package game.main.stat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Stats implements HasStats {
    public Array<Stat> stats;
    public Array<HasStats> attached;

    public Stats() {
        stats = new Array<Stat>();
        attached = new Array<HasStats>();
    }

    public void getStats(Stat.Type type, Array<Stat> stats) {
        for (Stat s : this.stats) {
            if (s.type == type) {
                stats.add(s);
            }
        }

        for (HasStats hasStats : attached) {
            hasStats.getStats().getStats(type, stats);
        }
    }

    public Array<Stat> getStats(Stat.Type type) {
        Array<Stat> stats = new Array<Stat>();
        getStats(type, stats);
        return stats;
    }

    public float getValue(Stat.Type type) {
        float value = 0;

        for (Stat s : getStats(type)) {
            value += s.value;
        }

        return value;
    }

    public float getMultiplier(Stat.Type type) {
        float multiplier = 0;

        for (Stat s : getStats(type)) {
            multiplier += s.multiplier;
        }

        return multiplier;
    }

    public float get(Stat.Type type) {
        return getValue(type) * (MathUtils.clamp(1 + getMultiplier(type), 0, Float.MAX_VALUE));
    }

    public void attach(HasStats stats) {
        attached.add(stats);
    }

    public void add(Stat s) {
        stats.add(s);
    }

    @Override
    public Stats getStats() {
        return this;
    }
}
