package game.main.positionable.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.SpriteSheet;
import game.main.item.Item;

public class Chest extends Entity implements Interactable {
    public SpriteSheet spriteSheet;
    public boolean opened;

    public Chest() {
        hitbox.set(0, 0, 8, 4);
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
    public boolean isAttacking() {
        return false;
    }

    @Override
    public void interact(Entity interactor) {
        if (!opened) {
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
