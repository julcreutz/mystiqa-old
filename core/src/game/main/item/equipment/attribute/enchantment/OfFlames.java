package game.main.item.equipment.attribute.enchantment;

import game.main.stat.Stat;

public class OfFlames extends Enchantment {
    public OfFlames() {
        name = " of Flames";
        stats.add(new Stat(Stat.Type.FIRE_DAMAGE, .5f));
    }
}
