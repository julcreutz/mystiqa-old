package mystiqa.item.equipable.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;
import mystiqa.main.Game;

public abstract class Hand extends Equipable {
    public boolean behind;

    public int useState;
    public boolean using;

    public int step;

    public void use(Humanoid h) {
        if (useState == 0) {
            useState = 1;
            onBeginUse(h);
        }

        using = true;
    }

    public void update(Humanoid h) {
        if (!using && useState > 0) {
            useState = 0;
            onEndUse(h);
        }

        using = false;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(material.color);
    }

    public void onBeginUse(Humanoid h) {
    }

    public void onEndUse(Humanoid h) {
    }
}
