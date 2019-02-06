package game.main.item.equipment.hand.left;

import game.main.item.equipment.hand.Hand;
import game.main.positionable.entity.Player;

public class LeftHand extends Hand {
    @Override
    public void equip(Player p) {
        super.equip(p);

        p.leftHand = this;
    }

    @Override
    public void unequip(Player p) {
        super.unequip(p);

        p.leftHand = null;
    }
}
