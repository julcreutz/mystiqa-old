package mystiqa.item.equipable.armor;

import mystiqa.entity.Humanoid;

public class HeadArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.headArmor = this;
    }
}
