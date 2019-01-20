package game.main.object.item.equipment.hand;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.object.item.equipment.Equipment;
import game.main.object.entity.Humanoid;

public abstract class Hand extends Equipment {
    public void update(Humanoid h) {
    }

    public void render(SpriteBatch batch, Humanoid h) {
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
