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

        switch (name) {
            case "MeleeWeapon":
                i = new MeleeWeapon();
                break;
            case "Shield":
                i = new Shield();
                break;
            case "FeetArmor":
                i = new FeetArmor();
                break;
            case "BodyArmor":
                i = new BodyArmor();
                break;
            case "HeadArmor":
                i = new HeadArmor();
                break;
        }

        return i;
    }
}
