package mystiqa.item.equipable.hand.right;

import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.hand.Hand;

public class RightHand extends Hand {
    @Override
    public void equip(Humanoid h) {
        h.rightHand = this;
    }
}
