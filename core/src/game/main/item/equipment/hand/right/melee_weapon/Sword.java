package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class Sword extends MeleeWeapon {
    public Sword() {
        spriteSheet = new SpriteSheet("sword");

        minDamage = 5;
        maxDamage = 7;
        criticalChance = .1f;
        speed = 1;
        angle = 135;
    }
}
