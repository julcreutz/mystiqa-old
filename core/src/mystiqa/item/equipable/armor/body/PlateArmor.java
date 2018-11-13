package mystiqa.item.equipable.armor.body;

import mystiqa.Resources;
import mystiqa.item.equipable.material.MaterialType;

public class PlateArmor extends BodyArmor {
    public PlateArmor() {
        name = "Plate Armor";

        materialType = MaterialType.METAL;

        graphics = Resources.getSpriteSheet("graphics/items/equipable/armor/body/plate_armor.png", 8, 8);
    }
}
