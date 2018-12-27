package game.main.play.entity.slime;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.Game;
import game.main.play.Play;
import game.main.play.entity.Entity;
import game.main.play.entity.Hitbox;

public class Slime extends Entity {
    public SlimeType type;
    public SlimeState state;

    public float time;

    public float jumpAngle;
    public float jumpSpeed;
    public float jumpTime;

    public float z;
    public float velZ;

    public Slime() {
        hitbox.set(6, 4, 1, 1);
        state = SlimeState.RANDOM_MOVEMENT;
    }

    @Override
    public void update(Play play) {
        switch (state) {
            case RANDOM_MOVEMENT:
                if (new Vector2(play.player.x, play.player.y).sub(x, y).len() < 24) {
                    state = SlimeState.FOLLOW_PLAYER;
                    break;
                }

                time -= Game.delta();

                if (time < 0) {
                    jump(MathUtils.random(360f), MathUtils.random(12f, 24f), MathUtils.random(1f, 2f));

                    velZ = 90;
                }

                break;
            case FOLLOW_PLAYER:
                time -= Game.delta();

                if (time < 0) {
                    jump(new Vector2(play.player.x, play.player.y).sub(x, y).angle(), new Vector2(play.player.x, play.player.y).sub(x, y).len() * 2.5f, MathUtils.random(.5f, 1f));
                }

                break;
            case JUMP_BEGIN:
                if (jumpTime <= 0) {
                    jumpTime = .1f;
                }

                jumpTime -= Game.delta();

                if (jumpTime < 0) {
                    state = SlimeState.JUMP;
                }

                break;
            case JUMP:
                velX = MathUtils.cosDeg(jumpAngle) * jumpSpeed;
                velY = MathUtils.sinDeg(jumpAngle) * jumpSpeed;

                z += velZ * Game.delta();
                velZ -= Game.delta() * 512f;

                if (z < 0) {
                    z = 0;
                    velZ = 0;

                    jumpSpeed = 0;

                    state = SlimeState.JUMP_END;
                }

                break;
            case JUMP_END:
                if (jumpTime <= 0) {
                    jumpTime = .1f;
                }

                jumpTime -= Game.delta();

                if (jumpTime < 0) {
                    state = SlimeState.RANDOM_MOVEMENT;
                }

                break;
        }

        super.update(play);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (z > 0) {
            batch.draw(type.sheet[1][1], x, y);
        }

        TextureRegion image = null;

        switch (state) {
            case RANDOM_MOVEMENT:
            case FOLLOW_PLAYER:
                image = type.sheet[0][0];
                break;
            case JUMP:
                image = type.sheet[0][1];
                break;
            case JUMP_BEGIN:
            case JUMP_END:
                image = type.sheet[1][0];
                break;
        }

        batch.draw(image, x, y + z);
    }

    @Override
    public String[] colors() {
        return type.colors;
    }

    public void jump(float jumpAngle, float jumpSpeed, float time) {
        this.jumpAngle = jumpAngle;
        this.jumpSpeed = jumpSpeed;

        velZ = 90;

        this.time = time;

        state = SlimeState.JUMP_BEGIN;
    }

    @Override
    public boolean isOnGround() {
        return z <= 0;
    }

    @Override
    public boolean isAttacking() {
        return state == SlimeState.JUMP_END;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return hitbox;
    }
}
