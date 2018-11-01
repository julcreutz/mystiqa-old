package mystiqa.item.equipable;

import com.badlogic.gdx.utils.JsonValue;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.entity.stat.IntegerStat;
import mystiqa.entity.stat.Stat;
import mystiqa.entity.stat.StatManager;
import mystiqa.item.Item;

public abstract class Equipable extends Item {
    public StatManager stats;

    public Equipable() {
        stats = new StatManager();
    }

    public abstract void equip(Humanoid h);

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
