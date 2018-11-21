package mystiqa.item.equipable.hand.right.melee_weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.hand.right.RightHand;
import mystiqa.main.Game;

public class MeleeWeapon extends RightHand {
    public TextureRegion[][] graphics;
    public TextureRegion t;

    public float x;
    public float y;
    public float z;

    public float rot;

    public float attackTime;
    public boolean attack;

    public int angle;
    public float speed;

    public MeleeWeaponAttackType attackType;

    public MeleeWeapon() {
        speed = 1;
    }

    @Override
    public void update(Humanoid h) {
        t = graphics[0][0];

        z = h.z;

        float widthDiff = t.getRegionWidth() - 8;

        float[][] x = new float[][] {
                {4, 3, 4, 5},
                {7, 7, 6, 5},
                {4, 5, 4, 2},
                {1, 2, 1, 3},
        };

        float[][] y = new float[][] {
                {-1, -1, -1, -1},
                {-2, -2, -1, -1},
                {-2, -3, -2, -2},
                {-2, -2, -2, -3},
        };

        if (attackTime > 0) {
            float attackHitboxDist = 0;

            switch (attackType) {
                case SLASH:
                    rot = h.dir * 90 - 135 + (1 - attackTime) * angle;

                    step = attackTime > .67f ? 1 : (attackTime > .33f ? 0 : 3);

                    this.x = h.x + x[h.dir][step];
                    this.y = h.y + y[h.dir][step];

                    attackHitboxDist = 6 + widthDiff;

                    break;
                case STAB:
                    rot = h.dir * 90;

                    float dist = (attackTime < .5f ? attackTime * 2f : 1 - (attackTime - .5f) * 2f) * (MathUtils.clamp(4f + widthDiff, 0, 16));

                    switch (MathUtils.round((dist / 4f) * 2f)) {
                        case 0:
                            step = 1;
                            break;
                        case 1:
                            step = 0;
                            break;
                        case 2:
                            step = 3;
                            break;
                    }

                    this.x = h.x + x[h.dir][step] + MathUtils.cosDeg(rot) * dist - MathUtils.cosDeg(rot) * widthDiff;
                    this.y = h.y + y[h.dir][step] + MathUtils.sinDeg(rot) * dist - (h.step % 2 != 0 ? 1 : 0) - MathUtils.sinDeg(rot) * widthDiff;

                    attackHitboxDist = dist + widthDiff;

                    break;
            }

            h.blockDirectionChange = true;

            h.attackHitbox.set(0, 0, 0, 0, 0, 0);

            if (attack) {
                h.velX *= 0;
                h.velY *= 0;

                attackTime -= Game.delta() * 4f * speed;
                if (attackTime < 0) {
                    attackTime = 0;
                    h.blockDirectionChange = false;
                    h.attacking = false;
                } else {
                    h.attackHitbox.set(2 + MathUtils.cosDeg(rot) * attackHitboxDist, 2 + MathUtils.sinDeg(rot) * attackHitboxDist, 0, 4, 4, 4);
                }
            } else {
                h.velX *= .5f;
                h.velY *= .5f;
            }
        } else {
            float[][] rot = new float[][] {
                    {0, -22.5f, 0, 22.5f},
                    {90, 90 - 22.5f, 90, 90 + 22.5f},
                    {180, 180 + 22.5f, 180, 180 - 22.5f},
                    {270, 270 - 22.5f, 270, 270 + 22.5f},
            };

            step = h.step;

            this.rot = rot[h.dir][step];
            this.x = h.x + x[h.dir][step] - MathUtils.cosDeg(this.rot) * widthDiff;
            this.y = h.y + y[h.dir][step] - MathUtils.sinDeg(this.rot) * widthDiff;
        }

        switch (h.dir) {
            case 0:
                behind = false;
                break;
            case 1:
                behind = true;
                break;
            case 2:
                behind = true;
                break;
            case 3:
                behind = false;
                break;
        }

        super.update(h);
    }

    @Override
    public void onBeginUse(Humanoid h) {
        super.onBeginUse(h);

        if (attackTime == 0) {
            attackTime = 1;
            attack = false;
        }
    }

    @Override
    public void onEndUse(Humanoid h) {
        super.onEndUse(h);

        if (attackTime == 1) {
            attack = true;
            h.attacking = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(t, x, y + z, 0, t.getRegionHeight() * .5f, t.getRegionWidth(), t.getRegionHeight(), 1, 1, rot);
    }
}
