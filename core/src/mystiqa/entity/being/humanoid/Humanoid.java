package mystiqa.entity.being.humanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.entity.being.Being;
import mystiqa.entity.being.humanoid.race.HumanoidRace;
import mystiqa.item.equipable.armor.body.BodyArmor;
import mystiqa.item.equipable.armor.feet.FeetArmor;
import mystiqa.item.equipable.armor.head.HeadArmor;
import mystiqa.stat.Damage;
import mystiqa.stat.IntegerStat;
import mystiqa.stat.MaxHealth;
import mystiqa.item.equipable.Equipable;
import mystiqa.item.equipable.hand.left.LeftHand;
import mystiqa.item.equipable.hand.right.RightHand;
import mystiqa.main.Game;
import mystiqa.main.screen.PlayScreen;

public class Humanoid extends Being {
    public HumanoidRace race;

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

    public boolean controlledByPlayer;

    public Humanoid() {
        hitbox.set(1, 1, 0, 6, 2, 6);
    }

    @Override
    public void update(PlayScreen play) {
        if (controlledByPlayer) {
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

            if (dir.x != 0 || dir.y != 0) {
                float a = dir.angle();
                float speed = 24;

                velX += MathUtils.cosDeg(a) * speed;
                velY += MathUtils.sinDeg(a) * speed;
            }

            if (rightHand != null && Gdx.input.isKeyPressed(Input.Keys.F)) {
                rightHand.use(this);
            }

            if (leftHand != null && Gdx.input.isKeyPressed(Input.Keys.D)) {
                leftHand.use(this);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
                velZ += 85;
            }
        } else {

        }

        step = MathUtils.round(stateTime * 7.5f) % 4;
        leftHandStep = leftHand != null ? leftHand.step : step;
        rightHandStep = rightHand != null ? rightHand.step : step;

        if (leftHand != null) {
            leftHand.update(this);
        }

        if (rightHand != null) {
            rightHand.update(this);
        }

        super.update(play);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        float y = this.y + (step % 2 != 0 ? -1 : 0) + z;

        if (leftHand != null && leftHand.behind) {
            leftHand.render(batch);
        }

        if (rightHand != null && rightHand.behind) {
            rightHand.render(batch);
        }

        switch (dir) {
            case 0:
                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 1 : 2) : 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 2 : 1) : 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0), dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[0][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.head[0][dir], x, y);

                if (headArmor != null) {
                    headArmor.render(batch, x, y, 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0), dir);
                }

                break;
            case 2:
                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 1 : 2) : 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 2 : 1) : 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0), dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[0][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.head[0][dir], x, y);

                if (headArmor != null) {
                    headArmor.render(batch, x, y, 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0), dir);
                }

                break;
            case 1:
                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 1 : 2) : 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 2 : 1) : 0, dir, true);
                }

                batch.setColor(race.color);
                batch.draw(race.body[0][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 2 : 1) : 0)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 2 : 1) : 0), dir, true);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 1 : 2) : 0)][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 1 : 2) : 0), dir);
                }

                batch.setColor(race.color);
                batch.draw(race.head[0][dir], x, y);

                if (headArmor != null) {
                    headArmor.render(batch, x, y, 0, dir);
                }

                break;
            case 3:
                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 1 : 2) : 0][dir], x, y);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 1 : 2) : 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.feet[step % 2 != 0 ? (step == 1 ? 2 : 1) : 0][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    feetArmor.render(batch, x, y, step % 2 != 0 ? (step == 1 ? 2 : 1) : 0, dir, true);
                }

                batch.setColor(race.color);
                batch.draw(race.body[0][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 0, dir);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (leftHandStep % 2 != 0 ? (leftHandStep == 1 ? 2 : 1) : 0), dir, true);
                }

                batch.setColor(race.color);
                batch.draw(race.body[1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0)][dir], x, y);

                if (bodyArmor != null) {
                    bodyArmor.render(batch, x, y, 1 + (rightHandStep % 2 != 0 ? (rightHandStep == 1 ? 1 : 2) : 0), dir);
                }

                batch.setColor(race.color);

                batch.draw(race.head[0][dir], x, y);

                if (headArmor != null) {
                    headArmor.render(batch, x, y, 0, dir);
                }

                break;
        }

        if (leftHand != null && !leftHand.behind) {
            leftHand.render(batch);
        }

        if (rightHand != null && !rightHand.behind) {
            rightHand.render(batch);
        }
    }

    @Override
    public void onMove() {
        super.onMove();

        if (velZ == 0) {
            if (!blockDirectionChange) {
                if (velX != 0 || velY != 0) {
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
            }

            stateTime += Game.getDelta() * (new Vector2(velX, velY).len() / 24f);
        } else {
            stateTime = 1 / 7.5f;
        }
    }

    @Override
    public void onGround() {
        super.onGround();
        stateTime = 2 / 7.5f;
    }

    @Override
    public int getDamage() {
        return countInteger(Damage.class);
    }

    @Override
    public int getMaxHealth() {
        return countInteger(MaxHealth.class);
    }

    public Array<Equipable> getEquipment() {
        Array<Equipable> equipment = new Array<Equipable>();

        if (feetArmor != null) {
            equipment.add(feetArmor);
        }

        if (bodyArmor != null) {
            equipment.add(bodyArmor);
        }

        if (headArmor != null) {
            equipment.add(headArmor);
        }

        if (leftHand != null) {
            equipment.add(leftHand);
        }

        if (rightHand != null) {
            equipment.add(rightHand);
        }

        return equipment;
    }

    public <T extends IntegerStat> int countInteger(Class<T> c) {
        int total = stats.countInteger(c);

        for (Equipable e : getEquipment()) {
            total += e.countInteger(c);
        }

        total += race.stats.countInteger(MaxHealth.class);

        return total;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("race")) {
            race = Resources.getHumanoidRace(json.getString("race"));
        }
    }
}
