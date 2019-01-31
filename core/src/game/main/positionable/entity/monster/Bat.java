package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.Hitbox;

public class Bat extends Monster {
    public enum State {
        IDLE, FOCUS_PLAYER, ATTACK_PLAYER
    }

    public State state;

    public float moveDir;
    public float moveSpeed;
    public float moveTime;

    public float focusTime;
    public float focusSpeed;

    public float attackAngle;
    public float attackTime;

    public SpriteSheet spriteSheet;
    public float animTime;
    public float scaleX;

    public Bat() {
        hitbox.set(3, 2, 4, 2);
        state = State.IDLE;
        spriteSheet = new SpriteSheet("bat", 2, 1);
        scaleX = 1;

        maxHealth = 7;
        damage = 3;
        defense = 1;
        speed = 1;
        experience = 3;
    }

    @Override
    public void update() {
        super.update();

        if (isStunned()) {
            return;
        }

        float angleToPlayer = new Vector2(map.player.x, map.player.y).sub(x, y).angle();
        float distToPlayer = new Vector2(map.player.x, map.player.y).sub(x, y).len();

        switch (state) {
            case IDLE:
                moveTime -= Game.getDelta();

                if (moveTime < 0) {
                    moveDir = MathUtils.random(360f);
                    moveSpeed = MathUtils.random(8f, 16f) * getSpeed();

                    moveTime = MathUtils.random(.5f, 2f);
                }

                velX += MathUtils.cosDeg(moveDir) * moveSpeed;
                velY += MathUtils.sinDeg(moveDir) * moveSpeed;

                if (distToPlayer < 32) {
                    state = State.FOCUS_PLAYER;
                }

                break;
            case FOCUS_PLAYER:
                if (distToPlayer > 24) {
                    velX += MathUtils.cosDeg(angleToPlayer) * (distToPlayer - 24) * 4f;
                    velY += MathUtils.sinDeg(angleToPlayer) * (distToPlayer - 24) * 4f;
                } else if (distToPlayer < 16) {
                    velX -= MathUtils.cosDeg(angleToPlayer) * (1 - distToPlayer / 16f) * 32f;
                    velY -= MathUtils.sinDeg(angleToPlayer) * (1 - distToPlayer / 16f) * 32f;
                }

                velX += MathUtils.cosDeg(angleToPlayer + 90f) * focusSpeed;
                velY += MathUtils.sinDeg(angleToPlayer + 90f) * focusSpeed;

                if (focusTime == 0) {
                    focusTime = MathUtils.random(1f, 2f);
                    focusSpeed = (MathUtils.randomBoolean(.5f) ? 1 : -1) * MathUtils.random(8f, 24f) * getSpeed();
                }

                focusTime -= Game.getDelta();

                if (focusTime < 0) {
                    focusTime = 0;
                    attackAngle = angleToPlayer;
                    state = State.ATTACK_PLAYER;
                }

                break;
            case ATTACK_PLAYER:
                velX += MathUtils.cosDeg(attackAngle) * 56f * getSpeed();
                velY += MathUtils.sinDeg(attackAngle) * 56f * getSpeed();

                if (attackTime == 0) {
                    attackTime = .5f;
                }

                attackTime -= Game.getDelta();

                if (attackTime < 0) {
                    attackTime = 0;
                    state = State.FOCUS_PLAYER;
                }

                break;
        }

        animTime += Game.getDelta() * MathUtils.clamp((new Vector2(velX, velY).len() / 16f), .5f, Float.MAX_VALUE);
    }

    @Override
    public void onMove() {
        super.onMove();

        if (velX > 0) {
            scaleX = 1;
        } else {
            scaleX = -1;
        }
    }

    @Override
    public boolean collidesWithSolidTiles() {
        return false;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return getHitbox();
    }

    @Override
    public boolean isAttacking() {
        return super.isAttacking() && state == State.ATTACK_PLAYER;
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
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion img = spriteSheet.grab(MathUtils.floor(animTime * 7.5f) % spriteSheet.getColumns(), 0);
        batch.draw(img, x, y, img.getRegionWidth() * .5f, img.getRegionHeight() * .5f,
                img.getRegionWidth(), img.getRegionHeight(), scaleX, 1, 0);
    }
}
