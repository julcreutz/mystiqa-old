package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.dungeon.Lock;

public class Door extends Entity {
    public SpriteSheet spriteSheet;

    public boolean horizontal;
    public boolean visible;

    public Lock lock;

    public Door() {
        hitbox.set(16, 16, 0, 0);

        visible = true;
    }

    @Override
    public void update() {
        super.update();

        lock.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (visible) {
            if (horizontal) {
                batch.draw(spriteSheet.sheet[0][0], x, y, 4, 4, 8, 8, 1, -1, 90);
                batch.draw(spriteSheet.sheet[0][0], x, y + 8, 4, 4, 8, 8, -1, -1, 90);
                batch.draw(spriteSheet.sheet[0][0], x + 8, y, 4, 4, 8, 8, 1, 1, 90);
                batch.draw(spriteSheet.sheet[0][0], x + 8, y + 8, 4, 4, 8, 8, -1, 1, 90);
            } else {
                batch.draw(spriteSheet.sheet[0][0], x, y, 4, 4, 8, 8, 1, 1, 0);
                batch.draw(spriteSheet.sheet[0][0], x + 8, y, 4, 4, 8, 8, -1, 1, 0);
                batch.draw(spriteSheet.sheet[0][0], x, y + 8, 4, 4, 8, 8, 1, -1, 0);
                batch.draw(spriteSheet.sheet[0][0], x + 8, y + 8, 4, 4, 8, 8, -1, -1, 0);
            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushing() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return visible;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public float getSortLevel() {
        return Float.MAX_VALUE;
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
