package game.loader.object;

import game.main.object.item.Item;
import game.main.object.item.collectable.Collectable;
import game.main.object.item.equipment.armor.Armor;
import game.main.object.item.equipment.hand.right.MeleeWeapon;
import game.main.object.item.equipment.hand.left.Shield;

public class ItemLoader extends ObjectLoader<Item> {
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
