package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class HandAxe extends MeleeWeapon {
    public HandAxe() {
        name = "Hand Axe";
        spriteSheet = new SpriteSheet("hand_axe");

        minDamage = 3;
        maxDamage = 7;
        criticalChance = .05f;
        speed = 0.67f;
        angle = 180;
    }
}
