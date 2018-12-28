package game.main.item.equipment;

import com.badlogic.gdx.utils.JsonValue;
import game.main.item.Item;
import game.main.stat.Stats;

public abstract class Equipment extends Item {
    public Stats stats;

    public Equipment() {
        stats = new Stats();
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
