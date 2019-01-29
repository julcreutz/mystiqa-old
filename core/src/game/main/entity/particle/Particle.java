package game.main.entity.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.main.entity.Entity;

public class Particle extends Entity {
    public TextureRegion image;

    public float scale;
    public float rot;

    @Override
    public void update() {
        super.update();

        if (scale < 0) {
            map.entities.entities.removeValue(this, true);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(image, x, y,
                image.getRegionWidth() * .5f, image.getRegionHeight() * .5f,
                image.getRegionWidth(), image.getRegionHeight(),
                scale, scale, rot);
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
