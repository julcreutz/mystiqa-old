package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;
import game.main.stat.Stat;

public class Shortsword extends MeleeWeapon {
    public Shortsword() {
        stats.stats.addAll(new Stat(Stat.Type.PHYSICAL_DAMAGE, 5), new Stat(Stat.Type.SPEED, 1));
        spriteSheet = new SpriteSheet("sword");
        angle = 0;
    }
}
