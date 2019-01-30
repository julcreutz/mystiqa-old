package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class Shortsword extends MeleeWeapon {
    public Shortsword() {
        spriteSheet = new SpriteSheet("sword");

        damage = 5;
        speed = 1;
    }
}
