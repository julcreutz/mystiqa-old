package mystiqa.item.equipable;

import mystiqa.entity.Humanoid;
import mystiqa.item.Item;

public abstract class Equipable extends Item {
    public abstract void equip(Humanoid h);
}
