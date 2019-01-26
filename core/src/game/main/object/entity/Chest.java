package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.object.item.Item;

public class Chest extends Entity {
    public SpriteSheet spriteSheet;
    public boolean opened;

    public Chest() {
        hitbox.set(8, 4, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(spriteSheet.grab(opened ? 1 : 0, 0), x, y);
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

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString());
        }
    }
}
