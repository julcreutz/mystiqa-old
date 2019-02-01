package game.main.positionable.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.lock.Lock;

public class Door extends Entity {
    public static float OPEN_SPEED = 2.5f;

    public SpriteSheet noKey;
    public SpriteSheet key;
    public SpriteSheet bossKey;
    public SpriteSheet wall;

    public SpriteSheet spriteSheet;

    public boolean horizontal;
    public boolean visible;
    public float openTime;

    public Lock lock;

    public boolean invisible;

    public Door() {
        hitbox.set(0, 0, 16, 16);
        noKey = new SpriteSheet("dungeon_door", 3, 1);
        key = new SpriteSheet("dungeon_door_key", 3, 1);
        bossKey = new SpriteSheet("dungeon_door_boss_key", 3, 1);
        wall = new SpriteSheet("dungeon_wall", 5, 4);
        visible = false;

        isPushable = false;
        isPushing = false;

        isVulnerable = false;
    }

    @Override
    public void update() {
        super.update();

        if (spriteSheet == null) {
            spriteSheet = noKey;
        }

        if (visible) {
            isSolid = true;

            if (openTime < 1) {
                openTime += Game.getDelta() * OPEN_SPEED;

                if (openTime >= 1) {
                    openTime = 1;
                }
            }
        } else {
            isSolid = false;

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
            TextureRegion image = spriteSheet.grab(MathUtils.floor(openTime * (spriteSheet.getColumns() - 1)), 0);

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
                batch.draw(wall.grab(0, 1), x, y + 16);
                batch.draw(wall.grab(0, 1), x, y + 8);
                batch.draw(wall.grab(0, 1), x, y);
                batch.draw(wall.grab(0, 1), x, y - 8);
                // Right walls
                batch.draw(wall.grab(2, 1), x + 8, y + 16);
                batch.draw(wall.grab(2, 1), x + 8, y + 8);
                batch.draw(wall.grab(2, 1), x + 8, y);
                batch.draw(wall.grab(2, 1), x + 8, y - 8);
            } else {
                // Bottom walls
                batch.draw(wall.grab(1, 2), x - 8, y);
                batch.draw(wall.grab(1, 2), x, y);
                batch.draw(wall.grab(1, 2), x + 8, y);
                batch.draw(wall.grab(1, 2), x + 16, y);
                // Top walls
                batch.draw(wall.grab(1, 0), x - 8, y + 8);
                batch.draw(wall.grab(1, 0), x, y + 8);
                batch.draw(wall.grab(1, 0), x + 8, y + 8);
                batch.draw(wall.grab(1, 0), x + 16, y + 8);
            }
        }
    }

    @Override
    public float getSortLevel() {
        return Float.MAX_VALUE;
    }
}
