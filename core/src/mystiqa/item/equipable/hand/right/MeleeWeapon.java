package mystiqa.item.equipable.hand.right;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.main.Game;

public class MeleeWeapon extends RightHand {
    public TextureRegion[][] graphics;
    public TextureRegion t;

    public float x;
    public float y;

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

        float widthDiff = t.getRegionWidth() - 16;

        float[][] x = new float[][] {
                {8, 6, 8, 10},
                {13, 13, 13, 11},
                {8, 10, 8, 5},
                {4, 4, 4, 7},
        };

        float[][] y = new float[][] {
                {-2, -3, -2, -2},
                {-1, -1, -1, 1},
                {-3, -4, -3, -3},
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

                    attackHitboxDist = 8 + widthDiff;

                    break;
                case STAB:
                    rot = h.dir * 90;

                    float dist = MathUtils.clamp(MathUtils.sin(attackTime * MathUtils.PI) * (4f + widthDiff), 0, 16);

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

            h.attackHitbox.set(0, 0, 0, 0);

            if (attack) {
                h.velX *= 0;
                h.velY *= 0;

                attackTime -= Game.getDelta() * 4f * speed;
                if (attackTime < 0) {
                    attackTime = 0;
                    h.blockDirectionChange = false;
                } else {
                    h.attackHitbox.set(MathUtils.cosDeg(rot) * attackHitboxDist, MathUtils.sinDeg(rot) * attackHitboxDist, 16, 16);
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
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(t, x, y, 0, t.getRegionHeight() * .5f, t.getRegionWidth(), t.getRegionHeight(), 1, 1, rot);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("graphics")) {
            graphics = Resources.getSpriteSheet(json.getString("graphics"));
        }

        if (json.has("angle")) {
            angle = json.getInt("angle");
        }

        if (json.has("speed")) {
            speed = json.getFloat("speed");
        }

        if (json.has("attackType")) {
            attackType = MeleeWeaponAttackType.valueOf(json.getString("attackType"));
        }
    }
}
