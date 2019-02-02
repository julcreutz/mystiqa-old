package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;

public class BattleAxe extends MeleeWeapon {
    public BattleAxe() {
        name = "Battle Axe";
        spriteSheet = new SpriteSheet("battle_axe");

        minDamage = 7;
        maxDamage = 13;
        speed = 0.67f;
        angle = 180;
    }
}
