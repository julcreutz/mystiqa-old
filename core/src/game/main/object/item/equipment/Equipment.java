package game.main.object.item.equipment;

import com.badlogic.gdx.utils.JsonValue;
import game.main.object.item.Item;
import game.main.stat.Stat;
import game.main.stat.StatCounter;
import game.main.stat.StatManager;

public abstract class Equipment extends Item implements StatCounter {
    public StatManager stats;

    public Equipment() {
        stats = new StatManager();
    }

    @Override
    public float count(Stat.Type type) {
        return stats.count(type);
    }

    @Override
    public StatManager getStats() {
        return stats;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue stats = json.get("stats");
        if (stats != null) {
            this.stats.deserialize(stats);
        }
    }
}
