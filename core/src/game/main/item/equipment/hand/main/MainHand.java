package game.main.item.equipment.hand.main;

import game.main.item.equipment.hand.Hand;
import game.main.state.play.map.entity.Humanoid;

public abstract class MainHand extends Hand {
    public void onStartAttack(Humanoid h) {

    }

    public void onAttack(Humanoid h) {

    }

    public void onFinishAttack(Humanoid h) {

    }

    public boolean isAttacking() {
        return false;
    }
}
