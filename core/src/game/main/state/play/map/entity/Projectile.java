package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;

public class Projectile extends Entity {
    public TextureRegion image;

    public float speed;
    public float dir;

    public Projectile() {
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void update() {
        super.update();

        velX += MathUtils.cosDeg(dir) * speed;
        velY += MathUtils.sinDeg(dir) * speed;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(image, x, y,
                image.getRegionWidth() * .5f, image.getRegionHeight() * .5f, image.getRegionWidth(), image.getRegionHeight(),
                1, 1, dir);
    }

    @Override
    public void onIsBlocked(Entity blocker) {
        super.onIsBlocked(blocker);

        map.entities.entities.removeValue(this, true);
    }

    @Override
    public void onHasHit(Entity hit) {
        super.onHasHit(hit);

        map.entities.entities.removeValue(this, true);
    }

    @Override
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public boolean collidesWithSolidTiles() {
        return false;
    }

    @Override
    public boolean collidesWithSolidEntities() {
        return false;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return getHitbox();
    }

    @Override
    public boolean isAttacking() {
        return true;
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
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue image = json.get("image");
        if (image != null) {
            this.image = Game.SPRITE_SHEETS.load(image.asString()).sheet[0][0];
        }

        JsonValue speed = json.get("speed");
        if (speed != null) {
            this.speed = speed.asFloat();
        }
    }
}
