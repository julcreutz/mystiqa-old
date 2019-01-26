package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;

public class Block extends Entity {
    public SpriteSheet spriteSheet;

    public float startX;
    public float startY;

    public boolean pushing;
    public float pushTime;
    public float pushDir;
    public float pushX;
    public float pushY;

    public Block() {
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void preUpdate() {
        super.preUpdate();

        pushing = false;
    }

    @Override
    public void update() {
        super.update();

        if (pushTime > 0) {
            if (pushTime > .5f) {
                pushX = x + MathUtils.cosDeg(pushDir) * 8f;
                pushY = y + MathUtils.sinDeg(pushDir) * 8f;

                pushTime = .5f;
            }

            if (pushTime == .5f) {
                if (MathUtils.round(x) != pushX) {
                    velX = pushX > x ? 24 : -24;
                }

                if (MathUtils.round(y) != pushY) {
                    velY = pushY > y ? 24 : -24;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(spriteSheet.grab(3, 3), x, y);
    }

    @Override
    public void onAdded() {
        super.onAdded();

        startX = x;
        startY = y;
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
    public boolean isSolid() {
        return true;
    }

    public float getStartDistance() {
        float diffX = x - startX;
        float diffY = y - startY;

        return MathUtils.round((float) Math.sqrt(diffX * diffX + diffY * diffY));
    }

    @Override
    public float getSortLevel() {
        return Float.MAX_VALUE;
    }

    @Override
    public void onCollision(Entity e) {
        super.onCollision(e);

        if (e == map.player && pushTime < .5f) {
            pushTime += Game.getDelta();
            pushDir = MathUtils.round(new Vector2(x, y).sub(e.x, e.y).angle() / 90f) * 90f;
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
