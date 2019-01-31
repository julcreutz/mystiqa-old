package game.main.item.equipment.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.Game;
import game.main.item.equipment.Equipment;
import game.main.positionable.entity.Player;

public abstract class Hand extends Equipment {
    public float useTime;
    public int useState;
    public boolean isUsing;

    public void update(Player h) {
        if (useTime > 0) {
            if (!isUsing) {
                onStartUse(h);
                isUsing = true;
            }

            onUse(h);

            useState--;

            if (useState < 0) {
                isUsing = false;
                onFinishUse(h);
                useState = 0;
                useTime = 0;
            }
        }
    }

    public void render(SpriteBatch batch, Player h) {
    }

    public void use(Player h) {
        useTime += Game.getDelta();
        useState++;
    }

    public void onStartUse(Player h) {

    }

    public void onUse(Player h) {

    }

    public void onFinishUse(Player h) {

    }

    public boolean isUsing() {
        return useTime > 0;
    }

    public boolean isAttacking() {
        return false;
    }

    public int getArmIndex() {
        return 0;
    }

    public boolean renderBehind() {
        return false;
    }

    public boolean isBlocking() {
        return false;
    }
}
