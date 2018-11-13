package mystiqa.item.equipable;

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
