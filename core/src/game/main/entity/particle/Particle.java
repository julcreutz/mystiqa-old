package game.main.entity.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.entity.Entity;

public class Particle extends Entity {
    public static class Attribute {
        public float min;
        public float max;

        public float gain;

        public float value;

        public boolean init;

        public void update() {
            if (!init) {
                init = true;
                value = MathUtils.random(min, max);
            }

            value += gain * Game.getDelta();
        }
    }

    public TextureRegion image;

    public Attribute dir;
    public Attribute speed;
    public Attribute scale;
    public Attribute rot;

    public Particle() {
        dir = new Attribute();
        speed = new Attribute();
        scale = new Attribute();
        rot = new Attribute();
    }

    @Override
    public void update() {
        super.update();

        super.velX += MathUtils.cosDeg(dir.value) * speed.value;
        super.velY += MathUtils.sinDeg(dir.value) * speed.value;

        if (scale.value < 0) {
            map.entities.entities.removeValue(this, true);
        }

        dir.update();
        speed.update();
        scale.update();
        rot.update();
    }

    @Override
    public void onAdded() {
        super.onAdded();

        dir.update();
        speed.update();
        scale.update();
        rot.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(image, x, y,
                image.getRegionWidth() * .5f, image.getRegionHeight() * .5f,
                image.getRegionWidth(), image.getRegionHeight(),
                scale.value, scale.value, rot.value);
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
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public boolean removeOnDisabled() {
        return true;
    }
}
