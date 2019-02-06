package game.main.item.equipment.hand.right;

import game.main.item.equipment.hand.Hand;
import game.main.positionable.entity.Player;

public abstract class RightHand extends Hand {
    @Override
    public void equip(Player p) {
        super.equip(p);

        p.rightHand = this;
    }

    @Override
    public void unequip(Player p) {
        super.unequip(p);

        p.rightHand = null;
    }
}
