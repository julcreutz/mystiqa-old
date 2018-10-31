package mystiqa.item.equipable.armor;

import mystiqa.entity.humanoid.Humanoid;

public class BodyArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.bodyArmor = this;
    }
}
