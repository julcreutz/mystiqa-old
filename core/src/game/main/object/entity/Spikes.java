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

    public int state;

    public Spikes() {
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void update() {
        super.update();

        state = MathUtils.round(Game.time * (spriteSheet.getColumns() - 1)) % (spriteSheet.getColumns() * 2);

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
        return state == 2 || state == 3;
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
