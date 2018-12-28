package game.main.item.equipment.hand.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.play.entity.humanoid.Humanoid;
import game.main.stat.RelativeStat;
import game.main.stat.StatType;

public class MeleeWeapon extends MainHand {
    public static final float[][] X = new float[][] {
            {5, 6, 5, 3},
            {3, 0, 3, 4},
            {-5, -5, -5, -3},
            {-3, 0, -3, -5}
    };

    public static final float[][] Y = new float[][] {
            {-1, 1, -1, -3},
            {3, 2, 3, 1},
            {0, 1, 0, -2},
            {-6, -6, -6, -5}
    };

    public static final float[][] ROT = new float[][] {
            {0, 22.5f, 0, -22.5f},
            {90, 90 + 22.5f, 90, 90 - 22.5f},
            {180, 180 - 22.5f, 180, 180 + 22.5f},
            {270, 270 + 22.5f, 270, 270 - 22.5f}
    };

    public TextureRegion image;

    public float angle;
    public float speed;

    public RelativeStat slowdown;

    public float attackTime;

    public float rot;
    public float dist;

    public float x;
    public float y;

    public boolean attacking;

    public MeleeWeapon() {
        slowdown = new RelativeStat(StatType.SPEED, 0);
    }

    @Override
    public void update(Humanoid h) {
        super.update(h);

        if (isAttacking()) {
            attackTime -= Game.delta() * 4f * speed;

            float a = h.dir * 90 - 135 + (1 - attackTime) * angle;
            dist = 6 + MathUtils.sin(attackTime * MathUtils.PI) * 2f;

            x = h.x + MathUtils.cosDeg(a) * dist;
            y = h.y + MathUtils.sinDeg(a) * dist;

            rot = MathUtils.round(a / 45f) * 45f;

            renderBehind = y > h.y + 4;

            if (attacking) {
                h.attackHitbox.set(8, 8, x - h.x, y - h.y);
            } else {
                h.attackHitbox.set(0, 0, 0, 0);
            }

            if (attackTime < 0) {
                attackTime = 0;

                if (h.stats.contains(slowdown)) {
                    h.stats.remove(slowdown);
                }

                attacking = false;
            }

            if (attackTime > .67f) {
                armIndex = 3;
            } else if (attackTime > .33f) {
                armIndex = 2;
            } else {
                armIndex = 1;
            }
        } else {
            x = h.x + X[h.dir][h.step];
            y = h.y + Y[h.dir][h.step];
            rot = ROT[h.dir][h.step];

            renderBehind = h.dir == 2 || h.dir == 1;
        }
    }

    @Override
    public void onUse(Humanoid h) {
        super.onUse(h);

        if (!attacking) {
            attackTime = 1;
            if (!h.stats.contains(slowdown)) {
                h.stats.add(slowdown);
            }
            slowdown.value = .5f;
        }
    }

    @Override
    public void onFinishUse(Humanoid h) {
        super.onStartUse(h);

        if (attackTime > 0) {
            attacking = true;
            slowdown.value = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        batch.draw(image, x, y, 4, 4, 8, 8, 1, 1, rot);
    }

    public boolean isAttacking() {
        return attackTime > 0;
    }

    @Override
    public boolean isUsing() {
        return super.isUsing() || isAttacking();
    }
}