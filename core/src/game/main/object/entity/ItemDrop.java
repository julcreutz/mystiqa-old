package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.object.item.Item;

public class ItemDrop extends Entity {
    public Item i;
    public float yOffset;

    public ItemDrop(Item i) {
        this.i = i;
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void update() {
        super.update();

        yOffset = MathUtils.sinDeg(Game.time * 180f);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(i.icon.grab(0, 0), x, y + yOffset);
    }

    @Override
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public boolean isPushing() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void onCollision(Entity e) {
        super.onCollision(e);

        if (e == map.player) {
            map.player.inventory.add(i);
            map.entities.entities.removeValue(this, true);
        }
    }
}
