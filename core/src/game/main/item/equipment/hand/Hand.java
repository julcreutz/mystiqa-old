package game.main.item.equipment.hand;

import game.main.Game;
import game.main.item.equipment.Equipment;

public class Hand extends Equipment {
    public float time;

    public boolean started;
    public int state;

    public void use() {
        time += Game.delta();
        state++;
    }

    public void update() {
        if (time > 0) {
            if (!started) {
                onUseStart();
                started = true;
            }

            onUse();

            state--;

            if (state < 0) {
                started = false;
                onUseEnd();
                state = 0;
                time = 0;
            }
        }
    }

    public void onUseStart() {
    }

    public void onUse() {
    }

    public void onUseEnd() {
    }
}
