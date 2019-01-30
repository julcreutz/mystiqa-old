package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;
import game.main.stat.Stat;

public class BattleAxe extends MeleeWeapon {
    public BattleAxe() {
        name = "Battle Axe";
        stats.stats.add(new Stat(Stat.Type.PHYSICAL_DAMAGE, 13));
        stats.stats.add(new Stat(Stat.Type.SPEED, 0.67f));
        spriteSheet = new SpriteSheet("battle_axe");
        angle = 180;
    }
}
