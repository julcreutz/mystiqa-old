package game.main.item.equipment.armor;

import game.SpriteSheet;

public class LeatherArmor extends Armor {
    public LeatherArmor() {
        feet = new SpriteSheet("leather_armor_feet", 4, 4);
        body = new SpriteSheet("leather_armor_body", 5, 4);

        defense = 2;
        speed = -.05f;
    }
}
