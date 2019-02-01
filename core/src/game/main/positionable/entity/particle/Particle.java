package game.main.positionable.entity.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.main.positionable.entity.Entity;

public class Particle extends Entity {
    public TextureRegion image;

    public float scale;
    public float rot;

    public Particle() {
        isPushable = false;
        isPushing = false;

        isVulnerable = false;
    }

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
    public boolean removeOnDisabled() {
        return true;
    }
}
