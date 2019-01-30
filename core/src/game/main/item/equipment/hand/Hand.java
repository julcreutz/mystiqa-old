package game.main.item.equipment.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.Game;
import game.main.item.equipment.Equipment;
import game.main.positionable.entity.Humanoid;

public abstract class Hand extends Equipment {
    public float useTime;
    public int useState;
    public boolean isUsing;

    public void update(Humanoid h) {
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

    public void render(SpriteBatch batch, Humanoid h) {
    }

    public void use(Humanoid h) {
        useTime += Game.getDelta();
        useState++;
    }

    public void onStartUse(Humanoid h) {

    }

    public void onUse(Humanoid h) {

    }

    public void onFinishUse(Humanoid h) {

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
