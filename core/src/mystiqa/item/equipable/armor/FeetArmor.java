package mystiqa.item.equipable.armor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.entity.being.humanoid.Humanoid;

public class FeetArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.feetArmor = this;
    }
}
