package game.main.positionable.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.Hitbox;
import game.main.positionable.entity.Entity;
import game.main.positionable.entity.particle.ParticleEmitter;

public class Projectile extends Entity {
    public SpriteSheet image;

    public float speed;
    public float dir;

    public ParticleEmitter particles;

    public Projectile() {
        hitbox.set(0, 0, 8, 8);
    }

    @Override
    public void update() {
        super.update();

        velX += MathUtils.cosDeg(dir) * speed;
        velY += MathUtils.sinDeg(dir) * speed;

        if (particles != null) {
            particles.x = x;
            particles.y = y;

            particles.map = map;

            particles.update();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (image != null) {
            TextureRegion image = this.image.grab(MathUtils.floor(Game.time * 20f) % this.image.getColumns(), 0);

            batch.draw(image, x, y, image.getRegionWidth() * .5f, image.getRegionHeight() * .5f,
                    image.getRegionWidth(), image.getRegionHeight(), 1, 1, dir);
        }
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
    public boolean removeOnDisabled() {
        return true;
    }
}
