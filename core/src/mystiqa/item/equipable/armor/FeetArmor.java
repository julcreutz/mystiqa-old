package mystiqa.item.equipable.armor;

import mystiqa.entity.Humanoid;
import mystiqa.item.equipable.armor.Armor;

public class FeetArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.feetArmor = this;
    }
}
