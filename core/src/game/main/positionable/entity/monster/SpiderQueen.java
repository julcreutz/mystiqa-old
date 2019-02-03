package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.positionable.entity.projectile.Projectile;
import game.resource.SpriteSheet;
import game.main.Game;

public class SpiderQueen extends Monster {
    public enum State {FLEE, PREPARE_JUMP, JUMP, PREPARE_STING, STING}
    public State state;

    public float fleeAnimTime;

    public float prepareJumpTime;

    public float jumpAngle;
    public float jumpSpeed;

    public float z;
    public float velZ;

    public float prepareStingTime;

    public float stingTime;

    public SpriteSheet spriteSheet;
    public TextureRegion image;
    public float scaleX;

    public SpiderQueen() {
        hitbox.set(0, 0, 8, 4);

        maxHealth = 40;
        maxHealthPerLevel = 6;
        minDamage = 5;
        maxDamage = 8;
        damagePerLevel = 2;
        defense = 3;
        defensePerLevel = 1;
        experience = 10;
        experiencePerLevel = 2;

        state = State.FLEE;

        spriteSheet = new SpriteSheet("spider_queen", 3, 3);

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
                float distToPlayer = new Vector2(map.player.x, map.player.y).sub(x, y).len();

                if (distToPlayer > 24f || distToPlayer < 8f) {
                    if (MathUtils.randomBoolean(.5f)) {
                        state = State.PREPARE_JUMP;
                    } else {
                        state = State.PREPARE_STING;
                    }
                } else {
                    float angleToPlayer = new Vector2(map.player.x, map.player.y).sub(x, y).angle();

                    velX += MathUtils.cosDeg(angleToPlayer + 180) * 48f;
                    velY += MathUtils.sinDeg(angleToPlayer + 180) * 48f;
                }

                fleeAnimTime += Game.getDelta();
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
                }

                image = spriteSheet.grab(1, 1);

                isPushing = false;
                isPushable = false;

                isVulnerable = false;

                break;
            case PREPARE_STING:
                if (prepareStingTime == 0) {
                    prepareStingTime = 1f;
                }

                if (prepareStingTime > 0) {
                    prepareStingTime -= Game.getDelta();

                    if (prepareStingTime < 0) {
                        prepareStingTime = 0;
                        state = State.STING;
                    }
                }

                if (prepareStingTime < .5f) {
                    image = spriteSheet.grab(0, 2);
                } else {
                    image = spriteSheet.grab(0, 0);
                }

                if (map.player.x > x) {
                    scaleX = 1;
                } else if (map.player.x < x) {
                    scaleX = -1;
                }

                break;
            case STING:
                if (stingTime == 0) {
                    stingTime = .5f;

                    Projectile p = new Projectile();
                    p.image = spriteSheet.grab(2, 2);
                    p.dir = new Vector2(map.player.x, map.player.y).sub(x, y).angle();
                    p.x = x + MathUtils.cosDeg(p.dir) * 4f;
                    p.y = y + MathUtils.sinDeg(p.dir) * 4f;
                    p.minDamage = 1;
                    p.maxDamage = 3;
                    p.fire = 1;
                    p.speed = 64f;
                    map.entities.addEntity(p);
                }

                if (stingTime > 0) {
                    stingTime -= Game.getDelta();

                    if (stingTime < 0) {
                        stingTime = 0;
                        state = State.FLEE;
                    }
                }

                image = spriteSheet.grab(1, 2);

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
