package game.main.item.equipment.hand.right.melee_weapon;

import game.resource.SpriteSheet;

public class Dagger extends MeleeWeapon {
    public Dagger() {
        spriteSheet = new SpriteSheet("dagger");

        minDamage = 2;
        maxDamage = 3;
        criticalChance = .1f;
        speed = 1;
        angle = 180;
    }
}
