package game.main.item.equipment;

import com.badlogic.gdx.utils.JsonValue;
import game.main.item.Item;
import game.main.stat.StatManager;

public abstract class Equipment extends Item {
    public StatManager statManager;

    public Equipment() {
        statManager = new StatManager();
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue stats = json.get("stats");
        if (stats != null) {
            statManager.deserialize(stats);
        }
    }
}
