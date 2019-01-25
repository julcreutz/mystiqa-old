package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.object.entity.particle.ParticleEmitter;

public class Projectile extends Entity {
    public SpriteSheet image;

    public float speed;
    public float dir;

    public ParticleEmitter particles;

    public Projectile() {
        hitbox.set(8, 8, 0, 0);
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
            TextureRegion image = this.image.sheet[MathUtils.floor(Game.time * 20f) % this.image.sheet.length][0];

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

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue image = json.get("image");
        if (image != null) {
            this.image = Game.SPRITE_SHEETS.load(image.asString());
        }

        JsonValue speed = json.get("speed");
        if (speed != null) {
            this.speed = speed.asFloat();
        }

        JsonValue particles = json.get("particles");
        if (particles != null) {
            this.particles = new ParticleEmitter(particles);
        }
    }
}
