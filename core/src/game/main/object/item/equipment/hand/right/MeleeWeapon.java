package game.main.object.item.equipment.hand.right;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.object.entity.Humanoid;
import game.main.object.entity.particle.Particle;
import game.main.stat.Stat;

public class MeleeWeapon extends RightHand {
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

    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float angle;
    public float speed;
    public float range;

    public Stat slowdown;

    public float attackTime;

    public float rot;
    public float dist;

    public float x;
    public float y;

    public boolean attacking;

    public int armIndex;
    public boolean renderBehind;

    public float fireParticleTime;

    public MeleeWeapon() {
        slowdown = new Stat(Stat.Type.SPEED);
    }

    @Override
    public void update(Humanoid h) {
        super.update(h);

        if (isAttacking()) {
            if (attacking) {
                attackTime -= Game.getDelta() * 4f * speed;
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

                if (h.stats.stats.contains(slowdown, true)) {
                    h.stats.stats.removeValue(slowdown, true);
                }

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

        image = spriteSheet.sheet[0][0];

        if (count(Stat.Type.FIRE_DAMAGE) > 0) {
            fireParticleTime -= Game.getDelta();

            if (fireParticleTime < 0) {
                fireParticleTime = MathUtils.random(.05f, .1f);

                Particle p = (Particle) Game.ENTITIES.load("Flame");
                p.x = x + MathUtils.random(-2, 2);
                p.y = y + MathUtils.random(-2, 2);

                h.map.entities.addEntity(p);

                p.dir.value = 90;
                p.speed.value = 12;
                p.rot.value = -90;
            }
        }
    }

    @Override
    public void onAttack(Humanoid h) {
        super.onAttack(h);

        if (!attacking) {
            attackTime = 1;
            attackTime = MathUtils.clamp(attackTime, 0, 1);
            if (!h.stats.stats.contains(slowdown, true)) {
                h.stats.stats.add(slowdown);
            }
            slowdown.multiplier = -.5f;
        }
    }

    @Override
    public void onFinishAttack(Humanoid h) {
        super.onFinishAttack(h);

        if (attackTime > 0) {
            attacking = true;
            slowdown.multiplier = -1;
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        ShaderProgram old = batch.getShader();

        if (count(Stat.Type.FIRE_DAMAGE) > 0) {
            batch.setShader(Game.SHADERS.load("Burning").shader);
        }

        batch.draw(image, x, y, 4, 4, image.getRegionWidth(), 8, 1, 1, rot);

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

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString());
        }

        JsonValue angle = json.get("angle");
        if (angle != null) {
            this.angle = angle.asFloat();
        }

        JsonValue speed = json.get("speed");
        if (speed != null) {
            this.speed = speed.asFloat();
        }

        JsonValue range = json.get("range");
        if (range != null) {
            this.range = range.asFloat();
        }
    }
}