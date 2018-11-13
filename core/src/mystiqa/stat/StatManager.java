package mystiqa.stat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class StatManager {
    public Array<Stat> stats;

    public StatManager() {
        stats = new Array<Stat>();
    }

    public void add(Stat stat) {
        stats.add(stat);
    }

    public <T extends IntegerStat> int countInteger(Class<T> c) {
        int count = 0;

        for (Stat stat : stats) {
            if (c.isInstance(stat)) {
                count += ((T) (stat)).value;
            }
        }

        return count;
    }
}
