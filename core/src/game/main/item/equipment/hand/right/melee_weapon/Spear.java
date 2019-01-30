package game.main.item.equipment.hand.right.melee_weapon;

import game.SpriteSheet;
import game.main.stat.Stat;

public class Spear extends MeleeWeapon {
    public Spear() {
        stats.stats.add(new Stat(Stat.Type.PHYSICAL_DAMAGE, 4));
        stats.stats.add(new Stat(Stat.Type.SPEED, 1));
        spriteSheet = new SpriteSheet("spear");
        angle = 0;
        range = 1;
    }
}
