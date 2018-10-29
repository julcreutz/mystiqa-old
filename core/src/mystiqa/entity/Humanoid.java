package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import mystiqa.Resources;
import mystiqa.item.equipable.armor.BodyArmor;
import mystiqa.item.equipable.armor.FeetArmor;
import mystiqa.item.equipable.armor.HeadArmor;
import mystiqa.item.equipable.hand.left.LeftHand;
import mystiqa.item.equipable.hand.right.RightHand;
import mystiqa.main.Game;

public class Humanoid extends Entity {
    public FeetArmor feetArmor;
    public BodyArmor bodyArmor;
    public HeadArmor headArmor;

    public LeftHand leftHand;
    public RightHand rightHand;

    public int dir;

    public int step;
    public int leftHandStep;
    public int rightHandStep;

    public float stateTime;

    public boolean blockDirectionChange;

    public TextureRegion[][] feet;
    public TextureRegion[][] body;
    public TextureRegion[][] head;

    public Humanoid() {
        hitbox.setSize(8, 7);

        feet = Resources.getSpriteSheet("HumanFeet");
        body = Resources.getSpriteSheet("HumanBody");
        head = Resources.getSpriteSheet("HumanHead");
    }

    @Override
    public void update() {
        hitbox.setPosition(x + 4, y + 2);

        step = MathUtils.round(stateTime * 7.5f) % 4;
        leftHandStep = leftHand != null ? leftHand.getHumanoidStep(this) : step;
        rightHandStep = rightHand != null ? rightHand.getHumanoidStep(this) : step;

        if (leftHand != null) {
            leftHand.update(this);
        }

        if (rightHand != null) {
            rightHand.update(this);
        }

        super.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

        switch (dir) {
            case 0:
                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                }

                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y);
                }

                if (leftHand != null) {
                    leftHand.render(batch);
                }

                batch.draw(body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y);
                }

                batch.draw(body[0][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[0][dir], x, y);
                }

                batch.draw(body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                }

                batch.draw(head[0][dir], x, y);
                if (headArmor != null) {
                    batch.draw(headArmor.graphics[0][dir], x, y);
                }

                if (rightHand != null) {
                    rightHand.render(batch);
                }

                break;
            case 2:
                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                }

                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y);
                }

                if (rightHand != null) {
                    rightHand.render(batch);
                }

                batch.draw(body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                }

                batch.draw(body[0][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[0][dir], x, y);
                }

                batch.draw(body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y);
                }

                batch.draw(head[0][dir], x, y);
                if (headArmor != null) {
                    batch.draw(headArmor.graphics[0][dir], x, y);
                }

                if (leftHand != null) {
                    leftHand.render(batch);
                }

                break;
            case 1:
                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                }

                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                }

                if (leftHand != null) {
                    leftHand.render(batch);
                }

                if (rightHand != null) {
                    rightHand.render(batch);
                }

                batch.draw(body[0][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[0][dir], x, y);
                }

                batch.draw(body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 2 : 1) : 0)][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 2 : 1) : 0)][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                }

                batch.draw(body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                }

                batch.draw(head[0][dir], x, y);
                if (headArmor != null) {
                    batch.draw(headArmor.graphics[0][dir], x, y);
                }

                break;
            case 3:
                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);
                }

                batch.draw(feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                if (feetArmor != null) {
                    batch.draw(feetArmor.graphics[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                }

                batch.draw(body[0][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[0][dir], x, y);
                }

                batch.draw(body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y, 8, 8, 16, 16, -1, 1, 0);
                }

                batch.draw(body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                if (bodyArmor != null) {
                    batch.draw(bodyArmor.graphics[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);
                }

                batch.draw(head[0][dir], x, y);
                if (headArmor != null) {
                    batch.draw(headArmor.graphics[0][dir], x, y);
                }

                if (leftHand != null) {
                    leftHand.render(batch);
                }

                if (rightHand != null) {
                    rightHand.render(batch);
                }

                break;
        }
    }

    @Override
    public void onMove() {
        super.onMove();

        if (!blockDirectionChange) {
            int _dir = MathUtils.round(new Vector2(velX, velY).angle() / 45f);

            switch (_dir) {
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

        stateTime += Game.getDelta() * (new Vector2(velX, velY).len() / 48f);
    }
}
