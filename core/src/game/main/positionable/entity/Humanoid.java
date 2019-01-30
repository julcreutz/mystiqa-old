package game.main.positionable.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.SpriteSheet;
import game.main.Game;
import game.main.item.equipment.armor.Armor;
import game.main.item.equipment.armor.PlateArmor;
import game.main.item.equipment.hand.left.HeaterShield;
import game.main.item.equipment.hand.right.RightHand;
import game.main.item.equipment.hand.left.LeftHand;
import game.main.item.equipment.hand.right.melee_weapon.BattleAxe;
import game.main.item.equipment.hand.right.melee_weapon.HandAxe;
import game.main.positionable.Hitbox;
import game.main.positionable.tile.Tile;

public class Humanoid extends Entity {
    public enum State {
        RANDOM_MOVEMENT, FOLLOW_PLAYER, ATTACK_PLAYER
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

    public float attackTime;

    public float yOffset;

    public float blockTime;

    public boolean changeDirection;

    public Humanoid() {
        hitbox.set(2, 1, 4, 2);
        attackHitbox = new Hitbox(this);
        blockHitbox = new Hitbox(this);

        maxHealth = 15;
        speed = 1;

        feet = new SpriteSheet("human_feet", 4, 4);
        body = new SpriteSheet("human_body", 5, 4);
        head = new SpriteSheet("human_head", 1, 4);

        rightHand = new BattleAxe();
        leftHand = new HeaterShield();
        armor = new PlateArmor();

        state = State.RANDOM_MOVEMENT;
    }

    @Override
    public void update() {
        if (blockTime > 0) {
            blockTime -= Game.getDelta();
        } else {
            if (blockTime < 0) {
                blockTime = 0;
            }
        }

        Vector2 dir = new Vector2();

        boolean useRightHand = false;
        boolean useLeftHand = false;

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
                useRightHand = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                useLeftHand = true;
            }

            if (dir.x != 0 || dir.y != 0) {
                moveAngle = dir.angle();
                moveSpeed = 1;
            } else {
                moveSpeed = 0;
            }
        } else {
            Vector2 toPlayer = new Vector2(map.player.x, map.player.y).sub(x, y);

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
                            state = State.ATTACK_PLAYER;

                            break;
                        }

                        moveAngle = toPlayer.angle();
                        moveSpeed = 1;
                    } else {
                        moveSpeed = 0;
                    }

                    break;
                case ATTACK_PLAYER:
                    if (toPlayer.len() < 24) {
                        useRightHand = true;
                    }

                    if (toPlayer.len() < 12) {
                        state = State.FOLLOW_PLAYER;
                        actionTime = MathUtils.random(.5f, 1f);

                        break;
                    }

                    moveAngle = toPlayer.angle();
                    moveSpeed = 1;

                    break;
            }
        }

        changeDirection = true;

        if (rightHand != null) {
            if (useRightHand) {
                rightHand.use(this);
            }

            rightHand.update(this);
        }

        if (leftHand != null) {
            if (useLeftHand) {
                leftHand.use(this);
            }

            leftHand.update(this);
        }

        if ((leftHand != null && leftHand.isUsing()) || (rightHand != null && rightHand.isUsing())) {
            lastUsed = 0;
        }

        if (moveSpeed != 0) {
            velX += MathUtils.round(MathUtils.cosDeg(moveAngle) * moveSpeed * 24f * getSpeed());
            velY += MathUtils.round(MathUtils.sinDeg(moveAngle) * moveSpeed * 24f * getSpeed());

            if (changeDirection && lastUsed > .01f) {
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
                batch.draw(feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.grab(1 + leftArmIndex % (body.getColumns() - 1), dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + leftArmIndex % (body.getColumns() - 1), dir), x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Torso
                batch.draw(body.grab(0, dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(0, dir), x, yOffset);
                }

                // Right foot
                batch.draw(feet.grab(step % feet.getColumns(), dir), x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab(step % feet.getColumns(), dir), x, yOffset);
                }

                // Head
                batch.draw(head.grab(step % head.getColumns(), dir), x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.grab(step % head.getColumns(), dir), x, yOffset);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.grab(1 + (rightArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + (rightArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                break;
            case 2:
                // Right foot
                batch.draw(feet.grab(step % feet.getColumns(), dir), x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab(step % feet.getColumns(), dir), x, yOffset);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.grab(1 + (rightArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + (rightArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Torso
                batch.draw(body.grab(0, dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(0, dir), x, yOffset);
                }

                // Left foot
                batch.draw(feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset);
                }

                // Head
                batch.draw(head.grab(step % head.getColumns(), dir), x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.grab(step % head.getColumns(), dir), x, yOffset);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.grab(1 + leftArmIndex % (body.getColumns() - 1), dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + leftArmIndex % (body.getColumns() - 1), dir), x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                break;
            case 1:
                // Left foot
                batch.draw(feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset);
                }

                // Right foot
                batch.draw(feet.grab(step % feet.getColumns(), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab(step % feet.getColumns(), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.grab(1 + (leftArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + (leftArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);
                }

                // Torso
                batch.draw(body.grab(0, dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(0, dir), x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.grab(1 + (rightArmIndex) % (body.getColumns() - 1), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + (rightArmIndex) % (body.getColumns() - 1), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Head
                batch.draw(head.grab(step % head.getColumns(), dir), x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.grab(step % head.getColumns(), dir), x, yOffset);
                }

                break;
            case 3:
                // Left foot
                batch.draw(feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab((step + 2) % feet.getColumns(), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right foot
                batch.draw(feet.grab(step % feet.getColumns(), dir), x, yOffset);

                if (armor != null && armor.feet != null) {
                    batch.draw(armor.feet.grab(step % feet.getColumns(), dir), x, yOffset);
                }

                // Torso
                batch.draw(body.grab(0, dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(0, dir), x, yOffset);
                }

                if (leftHand != null && leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                // Left arm
                batch.draw(body.grab(1 + (leftArmIndex) % (body.getColumns() - 1), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + (leftArmIndex) % (body.getColumns() - 1), dir), x, yOffset, 4, 4, 8, 8, -1, 1, 0);
                }

                if (rightHand != null && rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                // Right arm
                batch.draw(body.grab(1 + (rightArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);

                if (armor != null && armor.body != null) {
                    batch.draw(armor.body.grab(1 + (rightArmIndex + 2) % (body.getColumns() - 1), dir), x, yOffset);
                }

                // Head
                batch.draw(head.grab(step % head.getColumns(), dir), x, yOffset);

                if (armor != null && armor.head != null) {
                    batch.draw(armor.head.grab(step % head.getColumns(), dir), x, yOffset);
                }

                if (leftHand != null && !leftHand.renderBehind()) {
                    leftHand.render(batch, this);
                }

                if (rightHand != null && !rightHand.renderBehind()) {
                    rightHand.render(batch, this);
                }

                break;
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();

        map.entities.addListener(leftHand);
        map.entities.addListener(rightHand);
        map.entities.addListener(armor);
    }

    @Override
    public void onMove() {
        super.onMove();

        time += Game.getDelta() * (new Vector2(velX, velY).len() / 24f);
    }

    @Override
    public void onBlock(Entity e) {
        super.onBlock(e);

        blockTime = .1f;
    }

    @Override
    public boolean isAttacking() {
        return attackTime > 0 || (rightHand != null && rightHand.isAttacking())
                || (leftHand != null && leftHand.isAttacking());
    }

    @Override
    public boolean isBlocking() {
        return (rightHand != null && rightHand.isBlocking()) || (leftHand != null && leftHand.isBlocking());
    }

    @Override
    public Hitbox getAttackHitbox() {
        attackHitbox.position();
        return attackHitbox;
    }

    @Override
    public Hitbox getBlockHitbox() {
        blockHitbox.position();
        return blockHitbox;
    }

    @Override
    public float getDamage() {
        return super.getDamage() + (rightHand != null ? rightHand.damage : 0);
    }

    @Override
    public float getDefense() {
        return super.getDefense() + (armor != null ? armor.defense : 0);
    }

    @Override
    public float getFire() {
        return super.getFire() + (rightHand != null ? rightHand.fire : 0);
    }
}
