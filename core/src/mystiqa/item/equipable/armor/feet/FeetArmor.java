package mystiqa.item.equipable.armor.feet;

import mystiqa.entity.actor.humanoid.Humanoid;
import mystiqa.item.equipable.armor.Armor;

public class FeetArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.feetArmor = this;
    }
}
