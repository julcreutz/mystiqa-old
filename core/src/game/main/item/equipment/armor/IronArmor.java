package game.main.item.equipment.armor;

import game.resource.SpriteSheet;

public class IronArmor extends Armor {
    public IronArmor() {
        icon = new SpriteSheet("iron_armor_icon");

        feet = new SpriteSheet("iron_armor_feet", 4, 4);
        body = new SpriteSheet("iron_armor_body", 5, 4);

        defense = 2;
        speed = -.25f;
    }
}
