package mystiqa.entity.slime;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.main.Game;
import mystiqa.main.screen.PlayScreen;

public class Slime extends Entity {
    public SlimeState state;

    public TextureRegion[][] graphics;
    public TextureRegion t;
    public TextureRegion shadow;

    public float z;
    public float zVel;

    public float jumpSpeed;
    public float jumpAngle;

    public float attackTime;

    public float groundTime;

    public Slime() {
        super();

        hitbox.set(2, 2, 12, 7);

        state = SlimeState.IDLE;
    }

    @Override
    public void update(PlayScreen play) {
        switch (state) {
            case IDLE:
                if (nearestHostile != null) {
                    state = SlimeState.ATTACK_HOSTILE;
                    break;
                }

                break;
            case ATTACK_HOSTILE:
                attackTime -= Game.getDelta();

                if (attackTime < 0) {
                    Vector2 v = new Vector2(nearestHostile.x, nearestHostile.y).sub(x, y);

                    jumpSpeed = v.len() * 2f;
                    jumpAngle = v.angle();

                    zVel = 100;

                    attackTime = MathUtils.random(1f, 2f);

                    state = SlimeState.JUMP;
                    break;
                }

                break;
            case JUMP:
                if (z <= 0 && zVel <= 0) {
                    jumpSpeed = 0;
                    jumpAngle = 0;

                    state = SlimeState.IDLE;
                    break;
                }
                break;
        }

        velX += MathUtils.cosDeg(jumpAngle) * jumpSpeed;
        velY += MathUtils.sinDeg(jumpAngle) * jumpSpeed;

        z += zVel * Game.getDelta();

        zVel -= 500 * Game.getDelta();
        if (z < 0) {
            zVel = 0;
            z = 0;
        }

        if (z <= 0) {
            groundTime += Game.getDelta();
        } else {
            groundTime = 0;
        }

        if (z > 0) {
            t = graphics[0][1];
        } else if ((groundTime > 0 && groundTime < .1f) || attackTime < .1f) {
            t = graphics[1][0];
        } else {
            t = graphics[0][0];
        }

        shadow = graphics[1][1];

        super.update(play);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (z > 0) {
            batch.draw(shadow, x, y);
        }
        batch.draw(t, x, y + z);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("graphics")) {
            graphics = Resources.getSpriteSheet(json.getString("graphics"));
        }
    }
}
