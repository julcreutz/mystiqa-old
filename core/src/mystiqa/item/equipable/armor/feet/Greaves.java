package mystiqa.item.equipable.armor.feet;

import mystiqa.Resources;
import mystiqa.item.equipable.material.MaterialType;

public class Greaves extends FeetArmor {
    public Greaves() {
        name = "Greaves";

        materialType = MaterialType.METAL;

        graphics = Resources.getSpriteSheet("graphics/items/equipable/armor/feet/greaves.png", 8, 8);
    }
}
