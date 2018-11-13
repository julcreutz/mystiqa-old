package mystiqa.item.equipable.hand.right.melee_weapon;

import mystiqa.Resources;
import mystiqa.item.equipable.material.MaterialType;
import mystiqa.stat.Damage;

public class BattleAxe extends MeleeWeapon {
    public BattleAxe() {


        name = "Battle Axe";

        materialType = MaterialType.METAL;
        stats.add(new Damage(8));

        graphics = Resources.getSpriteSheet("graphics/items/equipable/hand/right/melee_weapons/battle_axe.png", 8, 8);

        angle = 180;
        speed = .75f;
        attackType = MeleeWeaponAttackType.SLASH;
    }
}
