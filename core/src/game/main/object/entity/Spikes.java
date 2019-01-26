package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;

public class Spikes extends Entity {
    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float time;
    public float timeScale;

    public Spikes() {
        hitbox.set(8, 8, 0, 0);
        timeScale = 1;
    }

    @Override
    public void update() {
        super.update();

        time += Game.getDelta() * timeScale;

        if (time > 1) {
            time = 1;
            timeScale = -1;
        } else if (time < 0) {
            time = 0;
            timeScale = 1;
        }

        image = spriteSheet.grab(MathUtils.round(time * (spriteSheet.getColumns() - 1)), 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(image, x, y);
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
    public float getSortLevel() {
        return Float.MAX_VALUE;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return getHitbox();
    }

    @Override
    public boolean isAttacking() {
        return time > .67f;
    }

    @Override
    public boolean isVulnerable() {
        return false;
    }

    @Override
    public boolean isBlockable() {
        return false;
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
