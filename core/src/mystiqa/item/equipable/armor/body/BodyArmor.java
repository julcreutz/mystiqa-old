package mystiqa.item.equipable.armor.body;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.armor.Armor;

public class BodyArmor extends Armor {
    @Override
    public void equip(Humanoid h) {
        h.bodyArmor = this;
    }
}
