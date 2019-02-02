package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class Longsword extends MeleeWeapon {
    public Longsword() {
        spriteSheet = new SpriteSheet("sword");

        minDamage = 5;
        maxDamage = 7;
        speed = 1;
        angle = 135;
        fire = .5f;
    }
}
