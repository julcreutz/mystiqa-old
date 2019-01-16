package game.main.state.play.map.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.item.equipment.armor.Armor;
import game.main.item.equipment.hand.right.RightHand;
import game.main.item.equipment.hand.left.LeftHand;
import game.main.stat.Stat;
import game.main.stat.StatCounter;
import game.main.state.play.map.tile.Tile;

public class Humanoid extends Entity {
    public enum State {
        RANDOM_MOVEMENT,
        FOLLOW_PLAYER,
        ATTACK_PLAYER,
        BLOCK,
        FLEE
    }

    public SpriteSheet feet;
    public SpriteSheet body;
    public SpriteSheet head;

    public Hitbox attackHitbox;
    public Hitbox blockHitbox;

    public RightHand rightHand;
    public LeftHand leftHand;

    public Armor armor;

    public int step;
    public int dir;
    public float time;

    public float lastUsed;

    public boolean controlledByPlayer;

    public State state;
    public float stateTime;

    public float moveAngle;
    public float moveSpeed;

    public float actionTime;
    public float blockTime;

    public float attackTime;

    public boolean attackStarted;
    public int attackState;

    public float yOffset;

    public Humanoid() {
        super();

        hitbox.set(4, 2, 2, 1);
        attackHitbox = new Hitbox(this);
        blockHitbox = new Hitbox(this);

        state = State.RANDOM_MOVEMENT;
    }

    @Override
    public void update() {
        Vector2 dir = new Vector2();

        boolean useMainHand = false;
        boolean useOffHand = false;

        if (controlledByPlayer) {
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

            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                useMainHand = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                useOffHand = true;
            }

            if (dir.x != 0 || dir.y != 0) {
                moveAngle = dir.angle();
                moveSpeed = 1;
            } else {
                moveSpeed = 0;
            }
        } else {
            Vector2 toPlayer = new Vector2(map.player.x, map.player.y).sub(x, y);

            /*
            switch (state) {
                case RANDOM_MOVEMENT:
                    if (toPlayer.len() < 32) {
                        state = State.FOLLOW_PLAYER;
                        break;
                    }

                    stateTime -= Game.getDelta();

                    if (stateTime < 0) {
                        stateTime = MathUtils.random(1f, 3f);

                        if (MathUtils.randomBoolean(.5f)) {
                            moveAngle = MathUtils.random(360f);
                            moveSpeed = 1;
                        } else {
                            moveSpeed = 0;
                        }
                    }

                    break;
                case FOLLOW_PLAYER:
                    if (toPlayer.len() > 64) {
                        state = State.RANDOM_MOVEMENT;
                        break;
                    }

                    actionTime -= Game.getDelta();

                    if (actionTime <= 0) {
                        if (toPlayer.len() < 16) {
                            if (MathUtils.randomBoolean(getHealthPercentage())) {
                                state = State.ATTACK_PLAYER;
                            } else {
                                if (leftHand != null) {
                                    state = State.BLOCK;
                                } else {
                                    state = State.FLEE;
                                }
                            }

                            break;
                        }

                        moveAngle = toPlayer.angle();
                        moveSpeed = 1;
                    } else {
                        moveSpeed = 0;
                    }

                    break;
                case ATTACK_PLAYER:
                    if (toPlayer.len() < 12) {
                        state = State.FOLLOW_PLAYER;
                        actionTime = MathUtils.random(.25f, .5f);
                        break;
                    }

                    rightHand.use();

                    moveAngle = toPlayer.angle();
                    moveSpeed = 1;

                    break;
                case BLOCK:
                    if (blockTime == 0) {
                        blockTime = MathUtils.random(.5f, 1f);
                    }

                    leftHand.use();

                    moveSpeed = -1;

                    blockTime -= Game.getDelta();

                    if (blockTime < 0) {
                        blockTime = 0;
                        state = State.FOLLOW_PLAYER;
                        break;
                    }

                    break;
                case FLEE:
                    if (blockTime == 0) {
                        blockTime = MathUtils.random(.5f, 1f);
                    }

                    moveSpeed = 1;
                    moveAngle = toPlayer.angle() + 180;

                    blockTime -= Game.getDelta();

                    if (blockTime < 0) {
                        blockTime = 0;
                        state = State.FOLLOW_PLAYER;
                        break;
                    }

                    break;
            }
            */
        }

        if (moveSpeed != 0) {
            velX = MathUtils.round(MathUtils.cosDeg(moveAngle) * moveSpeed * stats.count(Stat.Type.SPEED));
            velY = MathUtils.round(MathUtils.sinDeg(moveAngle) * moveSpeed * stats.count(Stat.Type.SPEED));

            if (lastUsed > .01f) {
                if (controlledByPlayer) {
                    switch (MathUtils.round((moveAngle + 360f) / 45f) % 8) {
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
                } else {
                    this.dir = MathUtils.round((moveAngle + 360f) / 90f) % 4;
                }
            }

            Tile t = tileAt();

            if (t != null && t.forcedDirection != -1) {
                this.dir = t.forcedDirection;
            }
        }

        if (rightHand != null) {
            if (useMainHand) {
                attack();
            }

            rightHand.update(this);
        }

        if (leftHand != null) {
            leftHand.update(this);
        }

        if (attackTime > 0) {
            if (!attackStarted) {
                onStartAttack();
                attackStarted = true;
            }

            onAttack();

            attackState--;

            if (attackState < 0) {
                attackStarted = false;
                onFinishAttack();
                attackState = 0;
                attackTime = 0;
            }
        }

        step = MathUtils.floor(time * 7.5f) % 4;

        lastUsed += Game.getDelta();

        if (isAttacking()) {
            lastUsed = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        yOffset = this.y + (step % 2 != 0 ? -1 : 0);

        int rightArmIndex = rightHand != null ? rightHand.getArmIndex() : step;
        int leftArmIndex = leftHand != null ? leftHand.getArmIndex() : step;

        switch (dir) {
            case 0:
                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[0][dir], x, yOffset);
                }

                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[step % feet.sheet.length][dir], x, yOffset);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.sheet[step % head.sheet.length][dir], x, yOffset);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                break;
            case 2:
                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[step % feet.sheet.length][dir], x, yOffset);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[0][dir], x, yOffset);
                }

                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.sheet[step % head.sheet.length][dir], x, yOffset);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                break;
            case 1:
                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset);
                }

                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[step % feet.sheet.length][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.sheet[1 + (leftArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + (leftArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[0][dir], x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex) % (body.sheet.length - 1)][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + (rightArmIndex) % (body.sheet.length - 1)][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.sheet[step % head.sheet.length][dir], x, yOffset);
                }

                break;
            case 3:
                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[(step + 2) % feet.sheet.length][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.sheet[step % feet.sheet.length][dir], x, yOffset);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[0][dir], x, yOffset);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.sheet[1 + (leftArmIndex) % (body.sheet.length - 1)][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + (leftArmIndex) % (body.sheet.length - 1)][dir], x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, yOffset);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.sheet[step % head.sheet.length][dir], x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                break;
        }

        //getAttackHitbox().render(batch);
    }

    public void attack() {
        attackTime += Game.getDelta();
        attackState++;
    }

    public void onStartAttack() {
        if (rightHand != null) {
            rightHand.onStartAttack(this);
        }
    }

    public void onAttack() {
        if (rightHand != null) {
            rightHand.onAttack(this);
        }
    }

    public void onFinishAttack() {
        if (rightHand != null) {
            rightHand.onFinishAttack(this);
        }
    }

    @Override
    public void onMove() {
        super.onMove();

        time += Game.getDelta() * (new Vector2(velX, velY).len() / 24f);
    }

    @Override
    public boolean isAttacking() {
        return attackTime > 0 || rightHand.isAttacking();
    }

    @Override
    public boolean isBlocking() {
        return (rightHand != null && rightHand.isBlocking()) || (leftHand != null && leftHand.isBlocking());
    }

    @Override
    public Hitbox getAttackHitbox() {
        return attackHitbox;
    }

    @Override
    public Hitbox getBlockHitbox() {
        return blockHitbox;
    }

    @Override
    public float count(Stat.Type type) {
        float value = 0;
        float multiplier = 0;

        StatCounter[] counters = new StatCounter[] {this, rightHand, leftHand, armor};

        for (StatCounter counter : counters) {
            for (Stat s : counter.getStats().stats) {
                if (s.type == type) {
                    value += s.value;
                    multiplier += s.multiplier;
                }
            }
        }

        return value * (1 + multiplier);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue feet = json.get("feet");
        if (feet != null) {
            this.feet = Game.SPRITE_SHEETS.load(feet.asString());
        }

        JsonValue body = json.get("body");
        if (body != null) {
            this.body = Game.SPRITE_SHEETS.load(body.asString());
        }

        JsonValue head = json.get("head");
        if (head != null) {
            this.head = Game.SPRITE_SHEETS.load(head.asString());
        }

        JsonValue armor = json.get("armor");
        if (this.armor != null && this.armor.feet != null) {
            this.armor = (Armor) Game.ITEMS.load(armor.asString());
        }

        JsonValue mainHand = json.get("rightHand");
        if (mainHand != null) {
            this.rightHand = (RightHand) Game.ITEMS.load(mainHand.asString());
        }

        JsonValue offHand = json.get("leftHand");
        if (offHand != null) {
            this.leftHand = (LeftHand) Game.ITEMS.load(offHand.asString());
        }
    }
}
