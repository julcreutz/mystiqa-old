package game.main.item.equipment.hand.right.melee_weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import game.resource.SpriteSheet;
import game.main.Game;
import game.main.positionable.entity.Player;
import game.main.positionable.entity.particle.Flame;
import game.main.item.equipment.hand.right.RightHand;

public class MeleeWeapon extends RightHand {
    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float speed;
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

    public boolean wasBlocking;

    public float damage;
    public float fire;

    @Override
    public void update(Player h) {
        super.update(h);

        if (isAttacking()) {
            // Manipulate player move speed
            if (attacking) {
                h.moveSpeed *= 0;
                attackTime -= Game.getDelta() * 4f * speed;
            } else {
                h.moveSpeed *= .5f;
            }

            // Calculate weapon angle
            float a;

            if (angle == 0) {
                a = h.dir * 90;
                dist = 6;
            } else {
                a = h.dir * 90 - 135 + (1 - attackTime) * angle;
                dist = 6 + MathUtils.sin(attackTime * MathUtils.PI) * (2f);
            }

            // Update position
            x = h.x + MathUtils.cosDeg(a) * dist;
            y = h.y + MathUtils.sinDeg(a) * dist + (h.yOffset - h.y);

            // Rotate image
            rot = a;

            // Choose whether to render behind or in front of player
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

            // End attacking
            if (attackTime < 0) {
                attackTime = 0;
                attacking = false;

                h.blocking = wasBlocking;
            }

            // Calculate player arm index
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

            this.x = h.x + new float[][] {
                    {5, 6, 5, 3},
                    {3, 0, 3, 4},
                    {-5, -6, -5, -3},
                    {-3, 0, -3, -5}
            }[h.dir][h.step];
            this.y = h.y + new float[][] {
                    {-1, 1, -1, -3},
                    {3, 2, 3, 1},
                    {-1, 1, -1, -3},
                    {-6, -6, -6, -5}
            }[h.dir][h.step];
            this.rot = new float[][] {
                    {0, 22.5f, 0, -22.5f},
                    {90, 90 + 22.5f, 90, 90 - 22.5f},
                    {180, 180 - 22.5f, 180, 180 + 22.5f},
                    {270, 270 + 22.5f, 270, 270 - 22.5f}
            }[h.dir][h.step];

            this.x -= MathUtils.cosDeg(this.rot) * (range * 4f);
            this.y -= MathUtils.sinDeg(this.rot) * (range * 4f);

            renderBehind = true;
        }

        // Update player attack hitbox
        if (attacking) {
            h.attackHitbox.set(x + MathUtils.cosDeg(rot) * range * 8f - h.x,
                    y + MathUtils.sinDeg(rot) * range * 8f - h.y, 8, 8);
        } else {
            h.attackHitbox.set(0, 0, 0, 0);
        }

        image = spriteSheet.grab(0, 0);

        // Spawn fire particles if needed
        if (fire > 0) {
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
    public void onStartUse(Player h) {
        super.onStartUse(h);

        wasBlocking = h.blocking;
        h.setBlocking(false);
    }

    @Override
    public void onUse(Player h) {
        super.onUse(h);

        if (!attacking) {
            attackTime = 1;
            attackTime = MathUtils.clamp(attackTime, 0, 1);
        }
    }

    @Override
    public void onFinishUse(Player h) {
        super.onFinishUse(h);

        if (attackTime > 0) {
            attacking = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, Player h) {
        super.render(batch, h);

        ShaderProgram old = batch.getShader();

        if (fire > 0) {
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

    @Override
    public void equip(Player p) {
        super.equip(p);

        p.damage += damage;
    }

    @Override
    public void unequip(Player p) {
        super.unequip(p);

        p.damage -= damage;
    }
}
