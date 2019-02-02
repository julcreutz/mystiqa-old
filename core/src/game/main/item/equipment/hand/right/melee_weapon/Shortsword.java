package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class Shortsword extends MeleeWeapon {
    public Shortsword() {
        spriteSheet = new SpriteSheet("sword");

        minDamage = 2;
        maxDamage = 4;
        speed = 1;
    }
}
