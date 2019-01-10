package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;

public class Block extends Entity {
    public SpriteSheet spriteSheet;

    public float startX;
    public float startY;

    public Block() {
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(spriteSheet.sheet[3][3], x, y);
    }

    @Override
    public void onAdded() {
        super.onAdded();

        startX = x;
        startY = y;
    }

    @Override
    public boolean isVulnerable() {
        return false;
    }

    public float getStartDistance() {
        float diffX = x - startX;
        float diffY = y - startY;

        return (float) Math.sqrt(diffX * diffX + diffY * diffY);
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
