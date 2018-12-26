package game.main.stat;

import com.badlogic.gdx.utils.Array;

public class Stats {
    public Array<AbsoluteStat> absolutes;
    public Array<RelativeStat> relatives;

    public Stats() {
        absolutes = new Array<AbsoluteStat>();
        relatives = new Array<RelativeStat>();
    }

    public float count(StatType type) {
        float total = 0;

        for (AbsoluteStat absolute : absolutes) {
            if (absolute.type == type) {
                total += absolute.value;
            }
        }

        float p = 1;

        for (RelativeStat relative : relatives) {
            if (relative.type == type) {
                p *= relative.value;
            }
        }

        return total * p;
    }

    public void add(AbsoluteStat s) {
        absolutes.add(s);
    }

    public void add(RelativeStat s) {
        relatives.add(s);
    }

    public void remove(AbsoluteStat s) {
        absolutes.removeValue(s, true);
    }

    public void remove(RelativeStat s) {
        relatives.removeValue(s, true);
    }

    public boolean contains(AbsoluteStat s) {
        return absolutes.contains(s, true);
    }

    public boolean contains(RelativeStat s) {
        return relatives.contains(s, true);
    }
}
