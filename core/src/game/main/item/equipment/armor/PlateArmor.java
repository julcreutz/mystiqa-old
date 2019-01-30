package game.main.item.equipment.armor;

import game.SpriteSheet;
import game.main.stat.Stat;

public class PlateArmor extends Armor {
    public PlateArmor() {
        stats.stats.add(new Stat(Stat.Type.PHYSICAL_DEFENSE, 9));
        feet = new SpriteSheet("plate_armor_feet", 4, 4);
        body = new SpriteSheet("plate_armor_body", 5, 4);
    }
}
