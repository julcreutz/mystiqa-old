package game.main.object.entity.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.object.entity.Entity;

public class Particle extends Entity {
    public static class Attribute implements Serializable {
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

        @Override
        public void deserialize(JsonValue json) {
            JsonValue min = json.get("min");
            if (min != null) {
                this.min = min.asFloat();
            }

            JsonValue max = json.get("max");
            if (max != null) {
                this.max = max.asFloat();
            }

            JsonValue gain = json.get("gain");
            if (gain != null) {
                this.gain = gain.asFloat();
            }
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
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue image = json.get("image");
        if (image != null) {
            this.image = Game.SPRITE_SHEETS.load(image.asString()).sheet[0][0];
        }

        JsonValue dir = json.get("dir");
        if (dir != null) {
            this.dir.deserialize(dir);
        }

        JsonValue speed = json.get("speed");
        if (speed != null) {
            this.speed.deserialize(speed);
        }

        JsonValue scale = json.get("scale");
        if (scale != null) {
            this.scale.deserialize(scale);
        }

        JsonValue rot = json.get("rot");
        if (rot != null) {
            this.rot.deserialize(rot);
        }
    }
}
