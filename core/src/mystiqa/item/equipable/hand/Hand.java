package mystiqa.item.equipable.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;
import mystiqa.main.Game;

public abstract class Hand extends Equipable {
    public boolean behind;

    public int useState;
    public boolean using;

    public float idleTime;

    public int step;

    public void use(Humanoid h) {
        if (useState == 0) {
            useState = 1;
            beginUse(h);
        }

        using = true;

        idleTime = 0;
    }

    public void update(Humanoid h) {
        if (!using && useState > 0) {
            useState = 0;
            endUse(h);
        }

        using = false;

        idleTime += Game.getDelta();
    }

    public void render(SpriteBatch batch) {

    }

    public void beginUse(Humanoid h) {
    }

    public void endUse(Humanoid h) {
    }
}
