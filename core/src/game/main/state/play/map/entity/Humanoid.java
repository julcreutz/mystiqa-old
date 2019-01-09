package game.main.state.play.map.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.item.equipment.hand.off.OffHand;
import game.main.item.equipment.hand.main.MainHand;
import game.main.item.equipment.armor.BodyArmor;
import game.main.item.equipment.armor.FeetArmor;
import game.main.item.equipment.armor.HeadArmor;
import game.main.stat.Stat;
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

    public MainHand mainHand;
    public OffHand offHand;

    public FeetArmor feetArmor;
    public BodyArmor bodyArmor;
    public HeadArmor headArmor;

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
                                if (offHand != null) {
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

                    mainHand.use();

                    moveAngle = toPlayer.angle();
                    moveSpeed = 1;

                    break;
                case BLOCK:
                    if (blockTime == 0) {
                        blockTime = MathUtils.random(.5f, 1f);
                    }

                    offHand.use();

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

        if (mainHand != null) {
            if (useMainHand) {
                mainHand.use();
            }

            mainHand.update(this);
        }

        if (offHand != null) {
            if (useOffHand && (mainHand == null || !mainHand.isUsing())) {
                offHand.use();
            }

            offHand.update(this);
        }

        step = MathUtils.floor(time * 7.5f) % 4;

        lastUsed += Game.getDelta();

        if ((mainHand != null && mainHand.isUsing()) || (offHand != null && offHand.isUsing())) {
            lastUsed = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

        int rightArmIndex = mainHand != null && mainHand.isUsing() ? mainHand.armIndex : step;
        int leftArmIndex = offHand != null && offHand.isUsing() ? offHand.armIndex : step;

        if (mainHand != null && mainHand.renderBehind) {
            mainHand.render(batch, this);
        }

        if (offHand != null && offHand.renderBehind) {
            offHand.render(batch, this);
        }

        switch (dir) {
            case 0:
                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y);
                }

                // Left arm
                batch.draw(body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                break;
            case 2:
                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                // Left arm
                batch.draw(body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);
                }

                break;
            case 1:
                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y);
                }

                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Left arm
                batch.draw(body.sheet[1 + (leftArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (leftArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                break;
            case 3:
                // Left foot
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right foot
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y);
                }

                // Torso
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Left arm
                batch.draw(body.sheet[1 + (leftArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (leftArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right arm
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                // Head
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                break;
        }

        if (mainHand != null && !mainHand.renderBehind) {
            mainHand.render(batch, this);
        }

        if (offHand != null && !offHand.renderBehind) {
            offHand.render(batch, this);
        }
    }

    @Override
    public void onMove() {
        super.onMove();

        time += Game.getDelta() * (new Vector2(velX, velY).len() / 24f);
    }

    @Override
    public boolean isAttacking() {
        return mainHand != null && mainHand.isUsing();
    }

    @Override
    public boolean isBlocking() {
        return offHand != null && offHand.isUsing();
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

        JsonValue feetArmor = json.get("feetArmor");
        if (feetArmor != null) {
            this.feetArmor = (FeetArmor) Game.ITEMS.load(feetArmor.asString());
        }

        JsonValue bodyArmor = json.get("bodyArmor");
        if (bodyArmor != null) {
            this.bodyArmor = (BodyArmor) Game.ITEMS.load(bodyArmor.asString());
        }

        JsonValue headArmor = json.get("headArmor");
        if (headArmor != null) {
            this.headArmor = (HeadArmor) Game.ITEMS.load(headArmor.asString());
        }

        JsonValue mainHand = json.get("mainHand");
        if (mainHand != null) {
            this.mainHand = (MainHand) Game.ITEMS.load(mainHand.asString());
        }

        JsonValue offHand = json.get("offHand");
        if (offHand != null) {
            this.offHand = (OffHand) Game.ITEMS.load(offHand.asString());
        }
    }
}
