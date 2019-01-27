package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;

public class Bat extends Entity {
    public enum State {
        IDLE
    }

    public State state;

    public float moveDir;
    public float moveSpeed;
    public float moveTime;

    public SpriteSheet spriteSheet;
    public float animTime;
    public float scaleX;

    public Bat() {
        hitbox.set(4, 2, 3, 2);
        state = State.IDLE;
        scaleX = 1;
    }

    @Override
    public void update() {
        super.update();

        switch (state) {
            case IDLE:
                moveTime -= Game.getDelta();

                if (moveTime < 0) {
                    moveDir = MathUtils.random(360f);
                    moveSpeed = MathUtils.random(12f, 24f);

                    moveTime = MathUtils.random(.5f, 2f);
                }

                velX += MathUtils.cosDeg(moveDir) * moveSpeed;
                velY += MathUtils.sinDeg(moveDir) * moveSpeed;

                break;
        }
    }

    @Override
    public void onMove() {
        super.onMove();

        animTime += Game.getDelta() * (new Vector2(velX, velY).len() / 16f);

        if (velX > 0) {
            scaleX = 1;
        } else {
            scaleX = -1;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion img = spriteSheet.grab(MathUtils.floor(animTime * 7.5f) % spriteSheet.getColumns(), 0);
        batch.draw(img, x, y, img.getRegionWidth() * .5f, img.getRegionHeight() * .5f,
                img.getRegionWidth(), img.getRegionHeight(), scaleX, 1, 0);
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
