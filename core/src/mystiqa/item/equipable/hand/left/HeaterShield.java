package mystiqa.item.equipable.hand.left;

import mystiqa.Resources;
import mystiqa.item.equipable.material.MaterialType;

public class HeaterShield extends Shield {
    public HeaterShield() {
        name = "Heater Shield";

        materialType = MaterialType.METAL;

        graphics = Resources.getSpriteSheet("graphics/items/equipable/hand/left/shields/heater_shield.png", 8, 8);
    }
}
