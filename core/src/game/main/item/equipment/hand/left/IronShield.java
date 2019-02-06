package game.main.item.equipment.hand.left;

import game.resource.SpriteSheet;

public class IronShield extends Shield {
    public IronShield() {
        spriteSheet = new SpriteSheet("items/shields/iron_shield", 1, 4);
        block = 13;
    }
}
