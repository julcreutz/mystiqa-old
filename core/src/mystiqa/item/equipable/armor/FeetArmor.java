package mystiqa.item.equipable.armor;

import mystiqa.entity.humanoid.Humanoid;

public class FeetArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.feetArmor = this;
    }
}
