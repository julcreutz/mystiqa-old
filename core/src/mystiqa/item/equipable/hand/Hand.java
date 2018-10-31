package mystiqa.item.equipable.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;

public abstract class Hand extends Equipable {
    public int useState;
    public boolean using;

    public void use(Humanoid h) {
        if (useState == 0) {
            useState = 1;
            beginUse(h);
        }

        using = true;
    }

    public void update(Humanoid h) {
        if (!using && useState > 0) {
            useState = 0;
            endUse(h);
        }

        using = false;
    }

    public void render(SpriteBatch batch) {

    }

    public void beginUse(Humanoid h) {
    }

    public void endUse(Humanoid h) {
    }

    public int getHumanoidStep(Humanoid h) {
        return h.step;
    }
}
