package game.main.item.equipment.hand.right.melee_weapon;

import game.resource.SpriteSheet;

public class BattleAxe extends MeleeWeapon {
    public BattleAxe() {
        name = "Battle Axe";
        icon = new SpriteSheet("battle_axe_icon");

        spriteSheet = new SpriteSheet("battle_axe");

        minDamage = 7;
        maxDamage = 9;
        criticalChance = .05f;
        speed = 0.67f;
        angle = 180;
    }
}
