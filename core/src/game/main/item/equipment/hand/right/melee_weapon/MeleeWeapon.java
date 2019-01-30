package game.main.item.equipment.hand.right.melee_weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import game.SpriteSheet;
import game.main.Game;
import game.main.entity.Humanoid;
import game.main.entity.particle.Flame;
import game.main.item.equipment.hand.right.RightHand;
import game.main.stat.Stat;

public class MeleeWeapon extends RightHand {
    public static final float[][] X = new float[][] {
            {5, 6, 5, 3},
            {3, 0, 3, 4},
            {-5, -6, -5, -3},
            {-3, 0, -3, -5}
    };

    public static final float[][] Y = new float[][] {
            {-1, 1, -1, -3},
            {3, 2, 3, 1},
            {-1, 1, -1, -3},
            {-6, -6, -6, -5}
    };

    public static final float[][] ROT = new float[][] {
            {0, 22.5f, 0, -22.5f},
            {90, 90 + 22.5f, 90, 90 - 22.5f},
            {180, 180 - 22.5f, 180, 180 + 22.5f},
            {270, 270 + 22.5f, 270, 270 - 22.5f}
    };

    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float angle;
    public float range;

    public float attackTime;

    public float rot;
    public float dist;

    public float x;
    public float y;

    public boolean attacking;

    public int armIndex;
    public boolean renderBehind;

    public float fireParticleTime;

    @Override
    public void update(Humanoid h) {
        super.update(h);

        if (isAttacking()) {
            if (attacking) {
                h.moveSpeed *= 0;
                attackTime -= Game.getDelta() * 4f * stats.get(Stat.Type.SPEED);
            } else {
                h.moveSpeed *= .5f;
            }

            float a;

            if (angle == 0) {
                a = h.dir * 90;
                dist = 6 - range * 4f + MathUtils.sin(attackTime * MathUtils.PI) * (range * 4f + 2f);
            } else {
                a = h.dir * 90 - 135 + (1 - attackTime) * angle;
                dist = 6 + MathUtils.sin(attackTime * MathUtils.PI) * (2f);
            }

            x = h.x + MathUtils.cosDeg(a) * dist;
            y = h.y + MathUtils.sinDeg(a) * dist + (h.yOffset - h.y);

            rot = a;

            switch (h.dir) {
                case 0:
                case 2:
                    renderBehind = true;
                    break;
                case 1:
                case 3:
                    renderBehind = y > h.y + 4;
                    break;
            }

            if (attackTime < 0) {
                attackTime = 0;

                attacking = false;
            }

            float relativeAngle = a - h.dir * 90f;

            if (relativeAngle < -90f) {
                armIndex = 3;
            } else if (relativeAngle < -45f) {
                armIndex = 2;
            } else {
                armIndex = 1;
            }
        } else {
            armIndex = h.step;

            x = h.x + X[h.dir][h.step];
            y = h.y + Y[h.dir][h.step];
            rot = ROT[h.dir][h.step];

            x -= MathUtils.cosDeg(rot) * (range * 4f);
            y -= MathUtils.sinDeg(rot) * (range * 4f);

            renderBehind = true;
        }

        if (attacking) {
            h.attackHitbox.set(8, 8,
                    x + MathUtils.cosDeg(rot) * range * 8f - h.x, y + MathUtils.sinDeg(rot) * range * 8f - h.y);
        } else {
            h.attackHitbox.set(0, 0, 0, 0);
        }

        image = spriteSheet.grab(0, 0);

        if (stats.get(Stat.Type.FIRE_DAMAGE) > 0) {
            fireParticleTime -= Game.getDelta();

            if (fireParticleTime < 0) {
                fireParticleTime = MathUtils.random(.05f, .1f);

                Flame p = new Flame();

                p.x = x + MathUtils.cosDeg(rot) * range * 8f + MathUtils.random(-2, 2);
                p.y = y + MathUtils.sinDeg(rot) * range * 8f + MathUtils.random(-2, 2);

                h.map.entities.addEntity(p);
            }
        }
    }

    @Override
    public void onUse(Humanoid h) {
        super.onUse(h);

        if (!attacking) {
            attackTime = 1;
            attackTime = MathUtils.clamp(attackTime, 0, 1);
        }
    }

    @Override
    public void onFinishUse(Humanoid h) {
        super.onFinishUse(h);

        if (attackTime > 0) {
            attacking = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        ShaderProgram old = batch.getShader();

        if (stats.get(Stat.Type.FIRE_DAMAGE) > 0) {
            batch.setShader(Game.SHADERS.load("burning"));
        }

        batch.draw(image, x, y, 4, image.getRegionHeight() * .5f, image.getRegionWidth(), image.getRegionHeight(), 1, 1, rot);

        batch.setShader(old);
    }

    public boolean isAttacking() {
        return attackTime > 0;
    }

    @Override
    public int getArmIndex() {
        return armIndex;
    }

    @Override
    public boolean renderBehind() {
        return renderBehind;
    }
}
