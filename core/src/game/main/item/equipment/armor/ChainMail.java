package game.main.item.equipment.armor;

import game.SpriteSheet;

public class ChainMail extends Armor {
    public ChainMail() {
        feet = new SpriteSheet("chain_mail_feet", 4, 4);
        body = new SpriteSheet("chain_mail_body", 5, 4);

        defense = 5;
        speed = -.1f;
    }
}
