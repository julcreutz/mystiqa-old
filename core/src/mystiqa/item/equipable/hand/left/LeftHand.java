package mystiqa.item.equipable.hand.left;

import mystiqa.entity.actor.humanoid.Humanoid;
import mystiqa.item.equipable.hand.Hand;

public class LeftHand extends Hand {
    @Override
    public void equip(Humanoid h) {
        h.leftHand = this;
    }
}
