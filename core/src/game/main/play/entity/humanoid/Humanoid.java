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

public class Humanoid extends Entity {
    public HumanoidType type;

    public OffHand offHand;
    public MainHand mainHand;

    public FeetArmor feetArmor;
    public BodyArmor bodyArmor;
    public HeadArmor headArmor;

    public int dir;
    public float time;

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
            velX = MathUtils.round(MathUtils.cosDeg(angle) * 24f);
            velY = MathUtils.round(MathUtils.sinDeg(angle) * 24f);

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

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            if (mainHand != null) {
                mainHand.use();
            }
        }

        if (mainHand != null) {
            mainHand.update();
        }

        if (offHand != null) {
            offHand.update();
        }

        super.update(site);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        int step = MathUtils.floor(time * type.animSpeed);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

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
                batch.draw(type.body[1 + step % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + step % (type.body.length - 1)][dir], x, y);
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
                batch.draw(type.body[1 + (step + 2) % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (step + 2) % (type.body.length - 1)][dir], x, y);
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
                batch.draw(type.body[1 + (step + 2) % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (step + 2) % (type.body.length - 1)][dir], x, y);
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
                batch.draw(type.body[1 + step % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + step % (type.body.length - 1)][dir], x, y);
                }

                break;
            case 1:
            case 3:
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
                batch.draw(type.body[1 + step % (type.body.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + step % (type.body.length - 1)][dir], x, y);
                }

                // Right arm
                batch.setShader(type.palette);
                batch.draw(type.body[1 + (step + 2) % (type.body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.sheet[1 + (step + 2) % (type.body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
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

        batch.setShader(null);
    }

    @Override
    public void onMove() {
        super.onMove();
        time += Game.delta() * (new Vector2(velX, velY).len() / 24f);
    }
}
