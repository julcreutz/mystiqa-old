package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.Game;
import game.main.positionable.entity.projectile.Projectile;
import game.resource.SpriteSheet;

public class Spider extends Monster {
    public enum State {FLEE, PREPARE_JUMP, JUMP}
    public State state;

    public float fleeTime;
    public float fleeAngle;
    public float fleeSpeed;
    public float fleeAnimTime;

    public float prepareJumpTime;

    public float jumpAngle;
    public float jumpSpeed;

    public float z;
    public float velZ;

    public SpriteSheet spriteSheet;
    public TextureRegion image;
    public float scaleX;

    public Spider() {
        hitbox.set(0, 0, 8, 4);

        maxHealth = 3;
        damage = 1;
        defense = 1;

        state = State.FLEE;

        spriteSheet = new SpriteSheet("entities/monsters/spider_queen", 3, 3);

        applyTileMovementSpeed = false;
    }

    @Override
    public void update() {
        super.update();

        isPushing = true;
        isPushable = true;

        isVulnerable = true;

        switch (state) {
            case FLEE:
                if (fleeTime == 0) {
                    if (new Vector2(map.player.x, map.player.y).sub(x, y).len() < 16) {
                        fleeTime = .5f;
                        fleeAngle = new Vector2(map.player.x, map.player.y).sub(x, y).angle() + 180;
                        fleeSpeed = 48f;
                    } else {
                        fleeTime = .25f;
                        fleeSpeed = 0;
                    }
                }

                if (fleeTime > 0) {
                    fleeTime -= Game.getDelta();

                    if (fleeTime < 0) {
                        fleeTime = 0;
                        state = State.PREPARE_JUMP;
                    }
                }

                velX += MathUtils.cosDeg(fleeAngle) * fleeSpeed;
                velY += MathUtils.sinDeg(fleeAngle) * fleeSpeed;

                if (fleeSpeed > 0) {
                    fleeAnimTime += Game.getDelta();
                }
                image = spriteSheet.grab(MathUtils.floor(fleeAnimTime * 10f) % 2, 0);

                if (velX > 0) {
                    scaleX = 1;
                } else if (velX < 0) {
                    scaleX = -1;
                }

                break;
            case PREPARE_JUMP:
                if (prepareJumpTime == 0) {
                    prepareJumpTime = 1f;
                }

                if (prepareJumpTime > 0) {
                    prepareJumpTime -= Game.getDelta();

                    if (prepareJumpTime < 0) {
                        prepareJumpTime = 0;
                        state = State.JUMP;
                    }
                }

                if (map.player.x > x) {
                    scaleX = 1;
                } else if (map.player.x < x) {
                    scaleX = -1;
                }

                if (prepareJumpTime < .5f) {
                    image = spriteSheet.grab(0, 1);
                } else {
                    image = spriteSheet.grab(0, 0);
                }

                break;
            case JUMP:
                isFlying = true;

                if (z == 0) {
                    velZ = 96f;

                    jumpAngle = new Vector2(map.player.x, map.player.y).sub(x, y).angle();
                    jumpSpeed = new Vector2(map.player.x, map.player.y).sub(x, y).len() * 2.5f;
                }

                velX += MathUtils.cosDeg(jumpAngle) * jumpSpeed;
                velY += MathUtils.sinDeg(jumpAngle) * jumpSpeed;

                z += velZ * Game.getDelta();

                velZ -= Game.getDelta() * 512;

                if (velZ < 0 && z < 0) {
                    z = 0;
                    state = State.FLEE;
                    isFlying = false;
                }

                image = spriteSheet.grab(1, 1);

                isPushing = false;
                isPushable = false;

                isVulnerable = false;

                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (z > 0) {
            batch.draw(spriteSheet.grab(2, 1), x, y);
        }

        if (image != null) {
            batch.draw(image, x, y + z, image.getRegionWidth() * .5f, image.getRegionHeight() * .5f,
                    image.getRegionWidth(), image.getRegionHeight(), scaleX, 1, 0);
        }
    }

    @Override
    public boolean isAttacking() {
        return MathUtils.floor(z) == 0 && velZ < 0 && state == State.JUMP;
    }
}
