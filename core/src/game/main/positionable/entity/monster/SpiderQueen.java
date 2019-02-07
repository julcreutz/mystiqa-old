package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.positionable.entity.projectile.Projectile;
import game.resource.SpriteSheet;
import game.main.Game;

public class SpiderQueen extends Monster {
    public static class Egg extends Monster {
        private SpriteSheet spriteSheet;
        private float time;

        public Egg() {
            hitbox.set(1, 1, 6, 4);

            maxHealth = 3;

            spriteSheet = new SpriteSheet("entities/monsters/spider_queen_egg");
            time = 5f;

            isPushable = false;
            isPushing = false;
        }

        @Override
        public void update() {
            super.update();

            time -= Game.getDelta();

            if (time < 0) {
                Spider s = new Spider();
                s.x = x;
                s.y = y;
                map.entities.addEntity(s);
                health = 0;
            }
        }

        @Override
        public void render(SpriteBatch batch) {
            super.render(batch);

            batch.draw(spriteSheet.grab(0, 0), x, y);
        }

        @Override
        public boolean isAttacking() {
            return false;
        }
    }

    public enum State {FLEE, PREPARE_JUMP, JUMP, PREPARE_STING, STING, PREPARE_EGG, EGG}
    public State state;
    public State lastState;

    public float fleeTime;
    public float fleeAngle;
    public float fleeSpeed;
    public float fleeAnimTime;

    public float prepareJumpTime;

    public float jumpAngle;
    public float jumpSpeed;

    public float z;
    public float velZ;

    public float prepareStingTime;

    public float stingTime;

    public float prepareEggTime;

    public SpriteSheet spriteSheet;
    public SpriteSheet sting;
    public TextureRegion image;
    public float scaleX;

    public SpiderQueen() {
        hitbox.set(0, 0, 16, 8);

        maxHealth = 30;
        damage = 2;

        state = State.FLEE;

        spriteSheet = new SpriteSheet("entities/monsters/spider_queen_large", 3, 4);
        sting = new SpriteSheet("entities/monsters/spider_queen_sting");

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
                        fleeTime = MathUtils.random(.25f, .5f);
                        fleeAngle = new Vector2(map.player.x, map.player.y).sub(x, y).angle() + 180;
                        fleeSpeed = 48f;
                    } else {
                        fleeTime = MathUtils.random(.25f, 1f);
                        fleeSpeed = 0;
                    }
                }

                if (fleeTime > 0) {
                    fleeTime -= Game.getDelta();

                    if (fleeTime < 0) {
                        fleeTime = 0;

                        if (lastState == null || lastState == State.PREPARE_JUMP || lastState == State.PREPARE_STING) {
                            if (MathUtils.randomBoolean(.25f)) {
                                state = State.PREPARE_EGG;
                            } else {
                                if (MathUtils.randomBoolean(.5f)) {
                                    state = State.PREPARE_JUMP;
                                } else {
                                    state = State.PREPARE_STING;
                                }
                            }
                        } else if (lastState == State.PREPARE_EGG) {
                            state = State.PREPARE_JUMP;
                        }

                        lastState = state;
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
                    p.setImage(sting.grab(0, 0));
                    p.x = x + 8;
                    p.y = y;
                    p.dir = new Vector2(map.player.x, map.player.y).sub(p.x, p.y).angle();
                    p.damage = 1;
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
            case PREPARE_EGG:
                if (prepareEggTime == 0) {
                    prepareEggTime = 1f;
                }

                if (prepareEggTime > 0) {
                    prepareEggTime -= Game.getDelta();

                    if (prepareEggTime < 0) {
                        prepareEggTime = 0;
                        state = State.EGG;
                    }
                }

                image = spriteSheet.grab(0, 3);

                break;
            case EGG:
                Egg e = new Egg();
                e.x = x + 4;
                e.y = y;
                map.entities.addEntity(e);
                state = State.FLEE;

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
