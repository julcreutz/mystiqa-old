package game.main.item.equipment.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.Game;
import game.main.item.equipment.Equipment;
import game.main.play.entity.humanoid.Humanoid;

public class Hand extends Equipment {
    public float useTime;

    public boolean started;
    public int state;

    public int armIndex;

    public boolean renderBehind;

    public void use() {
        useTime += Game.delta();
        state++;
    }

    public void update(Humanoid h) {
        if (useTime > 0) {
            if (!started) {
                onStartUse(h);
                started = true;
            }

            onUse(h);

            state--;

            if (state < 0) {
                started = false;
                onFinishUse(h);
                state = 0;
                useTime = 0;
            }
        }
    }

    public void onStartUse(Humanoid h) {
    }

    public void onUse(Humanoid h) {
    }

    public void onFinishUse(Humanoid h) {
    }

    public void render(SpriteBatch batch, Humanoid h) {
        batch.setShader(palette);
    }

    public boolean isUsing() {
        return useTime > 0;
    }
}
