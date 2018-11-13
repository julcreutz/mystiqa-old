package mystiqa.item.equipable.armor.head;

import mystiqa.Resources;
import mystiqa.item.equipable.material.MaterialType;

public class Helmet extends HeadArmor {
    public Helmet() {
        name = "Helmet";

        materialType = MaterialType.METAL;

        graphics = Resources.getSpriteSheet("graphics/items/equipable/armor/head/helmet.png", 8, 8);
    }
}
