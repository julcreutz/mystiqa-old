package mystiqa.item.equipable.armor.feet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.armor.Armor;

public class FeetArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.feetArmor = this;
    }
}
