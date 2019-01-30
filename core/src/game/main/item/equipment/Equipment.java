package game.main.item.equipment;

import game.main.entity.event.EntityEvent;
import game.main.entity.event.EntityListener;
import game.main.item.Item;
import game.main.item.equipment.attribute.enchantment.Enchantment;
import game.main.item.equipment.attribute.material.Material;
import game.main.stat.HasStats;
import game.main.stat.Stat;
import game.main.stat.Stats;

public abstract class Equipment extends Item implements HasStats, EntityListener {
    public Stats stats;

    public Material material;
    public Enchantment enchantment;

    public Equipment() {
        stats = new Stats();
    }

    @Override
    public void eventReceived(EntityEvent e) {
    }

    @Override
    public String getName() {
        return material.name + super.getName() + enchantment.name;
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    public void setMaterial(Material material) {
        this.material = material;
        stats.attach(material);
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
        stats.attach(enchantment);
    }
}
