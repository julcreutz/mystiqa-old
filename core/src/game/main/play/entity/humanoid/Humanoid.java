package game.main.play.entity.humanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.Game;
import game.main.item.equipment.hand.off.OffHand;
import game.main.item.equipment.hand.main.MainHand;
import game.main.item.equipment.armor.BodyArmor;
import game.main.item.equipment.armor.FeetArmor;
import game.main.item.equipment.armor.HeadArmor;
import game.main.play.Play;
import game.main.play.entity.Entity;
import game.main.stat.StatType;

public class Humanoid extends Entity {
    public HumanoidType type;

    public MainHand mainHand;
    public OffHand offHand;

    public FeetArmor feetArmor;
    public BodyArmor bodyArmor;
    public HeadArmor headArmor;

    public int step;
    public int dir;
    public float time;

    public float lastUsed;

    public Humanoid() {
        hitbox.set(4, 2, 2, 1);
    }

    @Override
    public void update(Play site) {
        Vector2 dir = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dir.x -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dir.x += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dir.y -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dir.y += 1;
        }

        float angle = dir.angle();

        if (dir.x != 0 || dir.y != 0) {
            float speed = stats.count(StatType.SPEED);

            velX = MathUtils.round(MathUtils.cosDeg(angle) * speed);
            velY = MathUtils.round(MathUtils.sinDeg(angle) * speed);

            if (lastUsed > .01f) {
                switch (MathUtils.floor((angle + 360f) / 45f) % 8) {
                    case 0:
                        this.dir = 0;
                        break;
                    case 2:
                        this.dir = 1;
                        break;
                    case 4:
                        this.dir = 2;
                        break;
                    case 6:
                        this.dir = 3;
                        break;
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            if (mainHand != null) {
                mainHand.use();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && (mainHand == null || !mainHand.using())) {
            if (offHand != null) {
                offHand.use();
            }
        }

        if (mainHand != null) {
            mainHand.update(this);
        }

        if (offHand != null) {
            offHand.update(this);
        }

        step = MathUtils.floor(time * type.animSpeed) % 4;

        lastUsed += Game.delta();

        if ((mainHand != null && mainHand.using()) || (offHand != null && offHand.using())) {
            lastUsed = 0;
        }

        super.update(site);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

        int rightArmIndex = mainHand != null && mainHand.using() ? mainHand.armIndex : step;
        int leftArmIndex = offHand != null && offHand.using() ? offHand.armIndex : step;

        if (mainHand != null && mainHand.renderBehind) {
            mainHand.render(batch, this);
        }

        if (offHand != null && offHand.renderBehind) {
            offHand.render(batch, this);
        }

        switch (dir) {
            case 0:
                // Left foot
                batch.setShader(type.palette);
                batch.draw(type.feet[(step + 2) % type.feet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[(step + 2) % type.feet.length][dir], x, y);
                }

                // Left arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + leftArmIndex % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + leftArmIndex % (type.body.length - 1)][dir], x, y);
                }

                // Torso
                batch.setShader(type.palette);
                batch.draw(type.body[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[0][dir], x, y);
                }

                // Right foot
                batch.setShader(type.palette);
                batch.draw(type.feet[step % type.feet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[step % type.feet.length][dir], x, y);
                }

                // Head
                batch.setShader(type.palette);
                batch.draw(type.head[step % type.head.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.sheet[step % type.head.length][dir], x, y);
                }

                // Right arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (rightArmIndex + 2) % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (rightArmIndex + 2) % (type.body.length - 1)][dir], x, y);
                }

                break;
            case 2:
                // Right foot
                batch.setShader(type.palette);
                batch.draw(type.feet[step % type.feet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[step % type.feet.length][dir], x, y);
                }

                // Right arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (rightArmIndex + 2) % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (rightArmIndex + 2) % (type.body.length - 1)][dir], x, y);
                }

                // Torso
                batch.setShader(type.palette);
                batch.draw(type.body[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[0][dir], x, y);
                }

                // Left foot
                batch.setShader(type.palette);
                batch.draw(type.feet[(step + 2) % type.feet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[(step + 2) % type.feet.length][dir], x, y);
                }

                // Head
                batch.setShader(type.palette);
                batch.draw(type.head[step % type.head.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.sheet[step % type.head.length][dir], x, y);
                }

                // Left arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + leftArmIndex % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + leftArmIndex % (type.body.length - 1)][dir], x, y);
                }

                break;
            case 1:
                // Left foot
                batch.setShader(type.palette);
                batch.draw(type.feet[(step + 2) % type.feet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[(step + 2) % type.feet.length][dir], x, y);
                }

                // Right foot
                batch.setShader(type.palette);
                batch.draw(type.feet[step % type.feet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[step % type.feet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Torso
                batch.setShader(type.palette);
                batch.draw(type.body[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[0][dir], x, y);
                }

                // Left arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (leftArmIndex + 2) % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (leftArmIndex + 2) % (type.body.length - 1)][dir], x, y);
                }

                // Right arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (rightArmIndex) % (type.body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (rightArmIndex) % (type.body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Head
                batch.setShader(type.palette);
                batch.draw(type.head[step % type.head.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.sheet[step % type.head.length][dir], x, y);
                }

                break;
            case 3:
                // Left foot
                batch.setShader(type.palette);
                batch.draw(type.feet[(step + 2) % type.feet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[(step + 2) % type.feet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right foot
                batch.setShader(type.palette);
                batch.draw(type.feet[step % type.feet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.sheet[step % type.feet.length][dir], x, y);
                }

                // Torso
                batch.setShader(type.palette);
                batch.draw(type.body[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[0][dir], x, y);
                }

                // Left arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (leftArmIndex) % (type.body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (leftArmIndex) % (type.body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (rightArmIndex + 2) % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (rightArmIndex + 2) % (type.body.length - 1)][dir], x, y);
                }

                // Head
                batch.setShader(type.palette);
                batch.draw(type.head[step % type.head.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.sheet[step % type.head.length][dir], x, y);
                }

                break;
        }

        if (mainHand != null && !mainHand.renderBehind) {
            mainHand.render(batch, this);
        }

        if (offHand != null && !offHand.renderBehind) {
            offHand.render(batch, this);
        }

        batch.setShader(null);
    }

    @Override
    public void onMove() {
        super.onMove();
        time += Game.delta() * (new Vector2(velX, velY).len() / 24f);
    }
}
