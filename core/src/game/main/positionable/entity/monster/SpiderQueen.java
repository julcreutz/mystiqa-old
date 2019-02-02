package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.SpriteSheet;
import game.main.Game;

public class SpiderQueen extends Monster {
    public enum State {FLEE, PREPARE, ATTACK}
    public State state;

    public float fleeAnimTime;

    public float prepareTime;

    public float attackAngle;
    public float attackSpeed;

    public float z;
    public float velZ;

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

        spriteSheet = new SpriteSheet("spider_queen", 5, 1);

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
                    state = State.PREPARE;
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
            case PREPARE:
                if (prepareTime == 0) {
                    prepareTime = 1;
                }

                if (prepareTime > 0) {
                    prepareTime -= Game.getDelta();

                    if (prepareTime < 0) {
                        prepareTime = 0;
                        state = State.ATTACK;
                    }
                }

                if (map.player.x > x) {
                    scaleX = 1;
                } else if (map.player.x < x) {
                    scaleX = -1;
                }

                if (prepareTime < .25f) {
                    image = spriteSheet.grab(2, 0);
                } else {
                    image = spriteSheet.grab(0, 0);
                }

                break;
            case ATTACK:
                if (z == 0) {
                    velZ = 96f;

                    attackAngle = new Vector2(map.player.x, map.player.y).sub(x, y).angle();
                    attackSpeed = new Vector2(map.player.x, map.player.y).sub(x, y).len() * 2.5f;
                }

                velX += MathUtils.cosDeg(attackAngle) * attackSpeed;
                velY += MathUtils.sinDeg(attackAngle) * attackSpeed;

                z += velZ * Game.getDelta();

                velZ -= Game.getDelta() * 512;

                if (velZ < 0 && z < 0) {
                    z = 0;
                    state = State.FLEE;
                }

                image = spriteSheet.grab(3, 0);

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
            batch.draw(spriteSheet.grab(4, 0), x, y);
        }

        batch.draw(image, x, y + z, image.getRegionWidth() * .5f, image.getRegionHeight() * .5f,
                image.getRegionWidth(), image.getRegionHeight(), scaleX, 1, 0);
    }

    @Override
    public boolean isAttacking() {
        return MathUtils.floor(z) == 0 && velZ < 0 && state == State.ATTACK;
    }
}
