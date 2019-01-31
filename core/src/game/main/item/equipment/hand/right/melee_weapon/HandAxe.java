package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class HandAxe extends MeleeWeapon {
    public HandAxe() {
        name = "Hand Axe";
        spriteSheet = new SpriteSheet("hand_axe");

        damage = 4;
        fire = 1;
        speed = 0.67f;
        angle = 180;
    }
}
