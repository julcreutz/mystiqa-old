package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.Hitbox;
import game.main.positionable.entity.Entity;

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

        isFlying = true;

        maxHealth = 7;
        maxHealthPerLevel = 1;
        minDamage = 2;
        maxDamage = 5;
        damagePerLevel = .5f;
        defense = 1;
        defensePerLevel = .5f;
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
                    moveSpeed = MathUtils.random(8f, 16f);

                    moveTime = MathUtils.random(.5f, 2f);
                }

                velX += MathUtils.cosDeg(moveDir) * moveSpeed;
                velY += MathUtils.sinDeg(moveDir) * moveSpeed;

                if (distToPlayer < 32) {
                    state = State.FOCUS_PLAYER;
                }

                animTime += Game.getDelta() * MathUtils.clamp((new Vector2(velX, velY).len() / 16f), .5f, Float.MAX_VALUE);

                if (velX > 0) {
                    scaleX = 1;
                } else if (velX < 0) {
                    scaleX = -1;
                }

                break;
            case FOCUS_PLAYER:
                if (distToPlayer < 16f) {
                    float speed = 12f * getSpeed();

                    velX += MathUtils.cosDeg(angleToPlayer + 180) * speed;
                    velY += MathUtils.sinDeg(angleToPlayer + 180) * speed;
                } else if (distToPlayer > 24f) {
                    float speed = 24f * getSpeed();

                    velX += MathUtils.cosDeg(angleToPlayer) * speed;
                    velY += MathUtils.sinDeg(angleToPlayer) * speed;
                }

                if (focusTime == 0) {
                    focusTime = MathUtils.random(1f, 3f);
                    focusSpeed = (MathUtils.randomBoolean(.5f) ? 1 : -1) * MathUtils.random(8f, 24f);
                }

                focusTime -= Game.getDelta();

                if (focusTime < 0) {
                    focusTime = 0;
                    attackAngle = angleToPlayer;
                    state = State.ATTACK_PLAYER;
                }

                animTime += Game.getDelta() * (focusTime > .5f ? 1 : 2);

                if (map.player.x > x) {
                    scaleX = 1;
                } else if (map.player.x < x) {
                    scaleX = -1;
                }

                break;
            case ATTACK_PLAYER:
                velX += MathUtils.cosDeg(attackAngle) * 56f;
                velY += MathUtils.sinDeg(attackAngle) * 56f;

                if (attackTime == 0) {
                    attackTime = distToPlayer * .025f;
                }

                attackTime -= Game.getDelta();

                if (attackTime < 0) {
                    attackTime = 0;
                    state = State.FOCUS_PLAYER;
                }

                animTime = 0;

                break;
        }
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
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion img = spriteSheet.grab(MathUtils.floor(animTime * 7.5f) % spriteSheet.getColumns(), 0);
        batch.draw(img, x, y, img.getRegionWidth() * .5f, img.getRegionHeight() * .5f,
                img.getRegionWidth(), img.getRegionHeight(), scaleX, 1, 0);
    }

    @Override
    public void onIsBlocked(Entity blocker) {
        super.onIsBlocked(blocker);

        if (state == State.ATTACK_PLAYER) {
            state = State.FOCUS_PLAYER;
            attackTime = 0;
        }
    }
}
