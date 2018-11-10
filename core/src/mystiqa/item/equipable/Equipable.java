package mystiqa.item.equipable;

import com.badlogic.gdx.utils.JsonValue;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.material.MaterialType;
import mystiqa.stat.IntegerStat;
import mystiqa.stat.StatManager;
import mystiqa.item.Item;
import mystiqa.item.equipable.material.Material;

public abstract class Equipable extends Item {
    public MaterialType materialType;
    public Material material;

    public StatManager stats;

    public Equipable() {
        stats = new StatManager();
    }

    public abstract void equip(Humanoid h);

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("materialType")) {
            materialType = MaterialType.valueOf(json.getString("materialType"));
        }

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }

    @Override
    public String getName() {
        return (material != null ? material.name + " " : "").concat(super.getName());
    }

    public <T extends IntegerStat> int countInteger(Class<T> c) {
        int total = stats.countInteger(c);

        if (material != null) {
            total += material.stats.countInteger(c);
        }

        return total;
    }
}
