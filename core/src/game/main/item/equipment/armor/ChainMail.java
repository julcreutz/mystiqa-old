package game.main.item.equipment.armor;

import game.SpriteSheet;

public class ChainMail extends Armor {
    public ChainMail() {
        feet = new SpriteSheet("plate_armor_feet", 4, 4);
        body = new SpriteSheet("plate_armor_body", 5, 4);

        defense = 5;
        speed = -.1f;
    }
}
