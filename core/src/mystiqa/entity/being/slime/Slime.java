package mystiqa.entity.being.slime;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import mystiqa.entity.being.Being;
import mystiqa.main.Game;
import mystiqa.main.screen.PlayScreen;

public class Slime extends Being {
    public SlimeState state;

    public TextureRegion[][] graphics;
    public TextureRegion t;
    public TextureRegion shadow;

    public float jumpSpeed;
    public float jumpAngle;

    public float attackTime;

    public float groundTime;

    public Color color;

    public Slime() {
        hitbox.set(1, 1, 0, 6, 2, 3);
        attackHitbox = hitbox;
        defendHitbox = hitbox;

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

                    velZ = 85;

                    attackTime = MathUtils.random(1f, 2f);

                    state = SlimeState.JUMP;
                    break;
                }

                break;
            case JUMP:
                if (velZ == 0) {
                    jumpSpeed = 0;
                    jumpAngle = 0;

                    state = SlimeState.IDLE;
                    break;
                }
                break;
        }

        velX += MathUtils.cosDeg(jumpAngle) * jumpSpeed;
        velY += MathUtils.sinDeg(jumpAngle) * jumpSpeed;

        if (velZ == 0) {
            groundTime += Game.getDelta();
        } else {
            groundTime = 0;
        }

        if (groundTime == 0) {
            t = graphics[0][1];
        } else if ((groundTime > 0 && groundTime < .1f) || attackTime < .1f) {
            t = graphics[1][0];
        } else {
            t = graphics[0][0];
        }

        shadow = graphics[1][1];

        attacking = true;
        //pushing = pushed = groundTime > .1f;

        super.update(play);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (z > 0) {
            //batch.draw(shadow, x, y);
        }

        batch.setColor(color);
        batch.draw(t, x, y + z);
    }
}
