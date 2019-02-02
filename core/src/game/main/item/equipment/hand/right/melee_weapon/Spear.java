package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class Spear extends MeleeWeapon {
    public Spear() {
        spriteSheet = new SpriteSheet("spear");

        minDamage = 3;
        maxDamage = 5;
        criticalChance = .05f;
        speed = 1;
        range = 1;
    }
}
