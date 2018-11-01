package mystiqa.stat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class StatManager {
    public Array<Stat> stats;

    public StatManager() {
        stats = new Array<Stat>();
    }

    public void deserialize(JsonValue json) {
        for (JsonValue data : json.iterator()) {
            String name = data.child.name;
            Stat stat = null;

            if (name.equals("MaxHealth")) {
                stat = new MaxHealth();
            } else if (name.equals("Damage")) {
                stat = new Damage();
            }

            if (stat != null) {
                stat.deserialize(data.child);
                stats.add(stat);
            }
        }
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
