package mystiqa.item.equipable.armor.body;

import mystiqa.entity.actor.humanoid.Humanoid;
import mystiqa.item.equipable.armor.Armor;

public class BodyArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.bodyArmor = this;
    }
}
