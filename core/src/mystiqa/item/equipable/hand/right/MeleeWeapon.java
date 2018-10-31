package mystiqa.item.equipable.hand.right;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.entity.Humanoid;
import mystiqa.main.Game;

public class MeleeWeapon extends RightHand {
    public TextureRegion[][] graphics;

    public float x;
    public float y;

    public float rot;

    public float attackTime;
    public boolean attack;

    @Override
    public void update(Humanoid h) {
        if (attackTime > 0) {
            rot = h.dir * 90 - 135 + (1 - attackTime) * 180;
            x = h.x + 8 + MathUtils.cosDeg(rot) * 4f;
            y = h.y + MathUtils.sinDeg(rot) * 4f - (h.step % 2 != 0 ? 1 : 0);

            h.blockDirectionChange = true;

            h.attackHitbox.set(0, 0, 0, 0);

            if (attack) {
                h.velX *= 0;
                h.velY *= 0;

                attackTime -= Game.getDelta() * 3.5f;
                if (attackTime < 0) {
                    attackTime = 0;
                    h.blockDirectionChange = false;
                } else {
                    h.attackHitbox.set(MathUtils.cosDeg(rot) * 8f, MathUtils.sinDeg(rot) * 8f, 16, 16);
                }

            } else {
                h.velX *= .5f;
                h.velY *= .5f;
            }
        } else {
            float[][] x = new float[][] {
                    {8, 6, 8, 10},
                    {13, 13, 13, 11},
                    {8, 10, 8, 5},
                    {4, 4, 4, 7},
            };

            float[][] y = new float[][] {
                    {-2, -3, -2, -2},
                    {-1, -1, -1, 1},
                    {-4, -5, -4, -4},
                    {-2, -2, -2, -3},
            };

            float[][] rot = new float[][] {
                    {0, -22.5f, 0, 22.5f},
                    {90, 90 - 22.5f, 90, 90 + 22.5f},
                    {180, 180 + 22.5f, 180, 180 - 22.5f},
                    {270, 270 - 22.5f, 270, 270 + 22.5f},
            };

            this.x = h.x + x[h.dir][h.step];
            this.y = h.y + y[h.dir][h.step];
            this.rot = rot[h.dir][h.step];
        }

        super.update(h);
    }

    @Override
    public void beginUse(Humanoid h) {
        super.beginUse(h);

        if (attackTime == 0) {
            attackTime = 1;
            attack = false;
        }
    }

    @Override
    public void endUse(Humanoid h) {
        super.endUse(h);

        if (attackTime == 1) {
            attack = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion t = graphics[0][0];
        batch.draw(t, x, y, 0, t.getRegionHeight() * .5f, t.getRegionWidth(), t.getRegionHeight(), 1, 1, rot);
    }

    @Override
    public int getHumanoidStep(Humanoid h) {
        return attackTime > 0 ? (attackTime > .67f ? 1 : (attackTime > .33f ? 0 : 3)) : super.getHumanoidStep(h);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("graphics")) {
            graphics = Resources.getSpriteSheet(json.getString("graphics"));
        }
    }
}
