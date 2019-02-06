package game.main.positionable.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.main.positionable.entity.monster.Monster;
import game.resource.SpriteSheet;
import game.main.Game;
import game.main.positionable.Hitbox;
import game.main.positionable.entity.Entity;
import game.main.positionable.entity.particle.ParticleEmitter;

public class Projectile extends Monster {
    public TextureRegion image;

    public float speed;
    public float dir;

    public ParticleEmitter particles;

    public Projectile() {
        hitbox.set(4, 4, 2, 2);

        isVulnerable = false;

        isPushable = false;

        collidesWithEntities = false;
        collidesWithTiles = false;

        isFlying = true;
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
    public Hitbox getAttackHitbox() {
        return getHitbox();
    }

    @Override
    public boolean isAttacking() {
        return true;
    }

    @Override
    public boolean removeOnDisabled() {
        return true;
    }
}
