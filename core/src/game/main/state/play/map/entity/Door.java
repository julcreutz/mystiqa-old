package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.dungeon.lock.Lock;

public class Door extends Entity {
    public static float OPEN_SPEED = 2.5f;

    public SpriteSheet spriteSheet;

    public boolean horizontal;
    public boolean visible;
    public float openTime;

    public Lock lock;

    public Door() {
        hitbox.set(16, 16, 0, 0);
        visible = false;
    }

    @Override
    public void update() {
        super.update();

        if (visible) {
            if (openTime < 1) {
                openTime += Game.getDelta() * OPEN_SPEED;

                if (openTime >= 1) {
                    openTime = 1;
                }
            }
        } else {
            if (openTime > 0) {
                openTime -= Game.getDelta() * OPEN_SPEED;

                if (openTime <= 0) {
                    openTime = 0;
                }
            }
        }

        if (lock != null) {
            lock.update();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion image = spriteSheet.sheet[MathUtils.floor(openTime * (spriteSheet.sheet.length - 1))][0];

        if (horizontal) {
            batch.draw(image, x, y, 4, 4, 8, 8, -1, 1, 270);
            batch.draw(image, x, y + 8, 4, 4, 8, 8, 1, 1, 270);
            batch.draw(image, x + 8, y, 4, 4, 8, 8, 1, 1, 90);
            batch.draw(image, x + 8, y + 8, 4, 4, 8, 8, -1, 1, 90);
        } else {
            batch.draw(image, x, y, 4, 4, 8, 8, 1, 1, 0);
            batch.draw(image, x + 8, y, 4, 4, 8, 8, -1, 1, 0);
            batch.draw(image, x, y + 8, 4, 4, 8, 8, -1, 1, 180);
            batch.draw(image, x + 8, y + 8, 4, 4, 8, 8, 1, 1, 180);
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
    public boolean isVulnerable() {
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
