package game.main.item.equipment.hand.left;

import game.resource.SpriteSheet;

public class RoundShield extends Shield {
    public RoundShield() {
        spriteSheet = new SpriteSheet("round_shield", 1, 4);
        block = 9;
    }
}
