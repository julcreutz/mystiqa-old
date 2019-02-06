package game.main.item.equipment.armor;

import game.resource.SpriteSheet;

public class IronArmor extends Armor {
    public IronArmor() {
        icon = new SpriteSheet("items/armors/iron_armor_icon");

        feet = new SpriteSheet("items/armors/iron_armor_feet", 4, 4);
        body = new SpriteSheet("items/armors/iron_armor_body", 5, 4);

        defense = 1;
    }
}
