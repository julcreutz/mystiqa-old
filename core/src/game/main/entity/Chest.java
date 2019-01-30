package game.main.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import game.SpriteSheet;
import game.main.Game;
import game.main.item.Item;

public class Chest extends Entity {
    public SpriteSheet spriteSheet;
    public boolean opened;

    public Chest() {
        hitbox.set(8, 4, 0, 0);
        spriteSheet = new SpriteSheet("chest", 2, 1);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(spriteSheet.grab(opened ? 1 : 0, 0), x, y);
    }

    @Override
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public void onCollision(Entity e) {
        super.onCollision(e);

        if (e == map.player && !opened) {
            opened = true;

            for (Item i : inventory) {
                ItemDrop drop = new ItemDrop(i);
                drop.x = x;
                drop.y = y;
                map.entities.addEntity(drop);
            }

            inventory.clear();
        }
    }
}
