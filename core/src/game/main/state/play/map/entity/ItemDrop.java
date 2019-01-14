package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.item.Item;

public class ItemDrop extends Entity {
    public Item i;

    public ItemDrop(Item i) {
        this.i = i;
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(i.icon.sheet[0][0], x, y);
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