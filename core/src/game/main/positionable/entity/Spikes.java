package game.main.positionable.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.Hitbox;

public class Spikes extends Entity {
    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public int state;
    public float offset;

    public Spikes() {
        hitbox.set(0, 0, 8, 8);
        spriteSheet = new SpriteSheet("spikes", 3, 1);

        isPushable = false;
        isPushing = false;

        isVulnerable = false;

        isBlockable = false;
    }

    @Override
    public void update() {
        super.update();

        state = MathUtils.round((Game.time + offset) * (spriteSheet.getColumns() - 1)) % (spriteSheet.getColumns() * 2);

        if (state < spriteSheet.getColumns()) {
            image = spriteSheet.grab(state, 0);
        } else {
            image = spriteSheet.grab(spriteSheet.getColumns() - 1 - (state - spriteSheet.getColumns()), 0);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(image, x, y);
    }

    @Override
    public float getSortLevel() {
        return Float.MAX_VALUE;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return getHitbox();
    }

    @Override
    public boolean isAttacking() {
        return state == 2 || state == 3;
    }
}
