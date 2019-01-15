package game.loader.instance;

import game.main.item.Item;
import game.main.item.collectable.Collectable;
import game.main.item.equipment.armor.Armor;
import game.main.item.equipment.hand.main.MeleeWeapon;
import game.main.item.equipment.hand.off.Shield;

public class ItemLoader extends InstanceLoader<Item> {
    @Override
    public Item newInstance(String name) {
        if (name.equals("MeleeWeapon")) {
            return new MeleeWeapon();
        } else if (name.equals("Shield")) {
            return new Shield();
        } else if (name.equals("Armor")) {
            return new Armor();
        } else if (name.equals("Collectable")) {
            return new Collectable();
        }

        return null;
    }
}
