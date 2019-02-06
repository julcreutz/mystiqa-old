package game.main.item.equipment.armor;

import game.main.positionable.entity.Player;
import game.resource.SpriteSheet;
import game.main.item.equipment.Equipment;

public class Armor extends Equipment {
    public SpriteSheet feet;
    public SpriteSheet body;
    public SpriteSheet head;

    public float defense;
    public float speed;

    @Override
    public void equip(Player p) {
        super.equip(p);

        p.armor = this;

        p.defense += defense;
        p.speed += speed;
    }

    @Override
    public void unequip(Player p) {
        super.unequip(p);

        p.armor = null;

        p.defense -= defense;
        p.speed -= speed;
    }
}
