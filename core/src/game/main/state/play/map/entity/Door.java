package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.dungeon.lock.KeyLock;
import game.main.state.play.map.dungeon.lock.Lock;

public class Door extends Entity {
    public static float OPEN_SPEED = 2.5f;

    public SpriteSheet spriteSheet;
    public SpriteSheet wall;

    public boolean horizontal;
    public boolean visible;
    public float openTime;

    public Lock lock;

    public boolean invisible;

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

        if (!invisible) {
            TextureRegion image = spriteSheet.sheet[MathUtils.floor(openTime * (spriteSheet.sheet.length - 1))][0];

            if (horizontal) {
                // Bottom left door
                batch.draw(image, x, y, 4, 4, 8, 8, -1, 1, 270);
                // Top left door
                batch.draw(image, x, y + 8, 4, 4, 8, 8, 1, 1, 270);
                // Bottom right door
                batch.draw(image, x + 8, y, 4, 4, 8, 8, 1, 1, 90);
                // Top right door
                batch.draw(image, x + 8, y + 8, 4, 4, 8, 8, -1, 1, 90);
            } else {
                // Bottom left door
                batch.draw(image, x, y, 4, 4, 8, 8, 1, 1, 0);
                // Bottom right door
                batch.draw(image, x + 8, y, 4, 4, 8, 8, -1, 1, 0);
                // Top left door
                batch.draw(image, x, y + 8, 4, 4, 8, 8, -1, 1, 180);
                // Top right door
                batch.draw(image, x + 8, y + 8, 4, 4, 8, 8, 1, 1, 180);
            }
        } else {
            if (horizontal) {
                // Left walls
                batch.draw(wall.sheet[0][1], x, y + 16);
                batch.draw(wall.sheet[0][1], x, y + 8);
                batch.draw(wall.sheet[0][1], x, y);
                batch.draw(wall.sheet[0][1], x, y - 8);
                // Right walls
                batch.draw(wall.sheet[2][1], x + 8, y + 16);
                batch.draw(wall.sheet[2][1], x + 8, y + 8);
                batch.draw(wall.sheet[2][1], x + 8, y);
                batch.draw(wall.sheet[2][1], x + 8, y - 8);
            } else {
                // Bottom walls
                batch.draw(wall.sheet[1][2], x - 8, y);
                batch.draw(wall.sheet[1][2], x, y);
                batch.draw(wall.sheet[1][2], x + 8, y);
                batch.draw(wall.sheet[1][2], x + 16, y);
                // Top walls
                batch.draw(wall.sheet[1][0], x - 8, y + 8);
                batch.draw(wall.sheet[1][0], x, y + 8);
                batch.draw(wall.sheet[1][0], x + 8, y + 8);
                batch.draw(wall.sheet[1][0], x + 16, y + 8);
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
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public float getSortLevel() {
        return Float.MAX_VALUE;
    }
}
