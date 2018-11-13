package mystiqa.item.equipable.armor.head;

import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.armor.Armor;

public class HeadArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.headArmor = this;
    }
}
