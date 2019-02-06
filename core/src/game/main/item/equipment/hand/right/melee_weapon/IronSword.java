package game.main.item.equipment.hand.right.melee_weapon;

import game.resource.SpriteSheet;

public class IronSword extends MeleeWeapon {
    public IronSword() {
        spriteSheet = new SpriteSheet("items/melee_weapons/iron_sword");

        damage = 1;
        speed = 1;
        angle = 135;
    }
}
