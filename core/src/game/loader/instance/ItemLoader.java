package game.loader.instance;

import game.main.item.Item;
import game.main.item.equipment.armor.BodyArmor;
import game.main.item.equipment.armor.FeetArmor;
import game.main.item.equipment.armor.HeadArmor;
import game.main.item.equipment.hand.main.MeleeWeapon;
import game.main.item.equipment.hand.off.Shield;

public class ItemLoader extends InstanceLoader<Item> {
    @Override
    public Item newInstance(String name) {
        Item i = null;

        if (name.equals("MeleeWeapon")) {
            i = new MeleeWeapon();
        } else if (name.equals("Shield")) {
            i = new Shield();
        } else if (name.equals("FeetArmor")) {
            i = new FeetArmor();
        } else if (name.equals("BodyArmor")) {
            i = new BodyArmor();
        } else if (name.equals("HeadArmor")) {
            i = new HeadArmor();
        }

        return i;
    }
}
