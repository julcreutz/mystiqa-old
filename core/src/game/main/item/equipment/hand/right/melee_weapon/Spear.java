package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class Spear extends MeleeWeapon {
    public Spear() {
        spriteSheet = new SpriteSheet("spear");

        damage = 4;
        speed = 1;
        range = 1;
    }
}
