package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.stat.Stat;
import game.main.state.play.map.entity.event.AddEvent;
import game.main.state.play.map.entity.event.DeathEvent;
import game.main.state.play.map.entity.event.EntityEvent;

public class Slime extends Entity {
    public enum State {
        RANDOM_MOVEMENT,
        FOLLOW_PLAYER,
        JUMP_BEGIN,
        JUMP,
        JUMP_END
    }

    public SpriteSheet spriteSheet;

    public State state;

    public float time;

    public float jumpAngle;
    public float jumpSpeed;
    public float jumpTime;

    public float z;
    public float velZ;

    public Array<String[]> splitInto;

    public Slime() {
        hitbox.set(6, 4, 1, 1);
        state = State.RANDOM_MOVEMENT;
    }

    @Override
    public void update() {
        switch (state) {
            case RANDOM_MOVEMENT:
                if (new Vector2(map.player.x, map.player.y).sub(x, y).len() < 24) {
                    state = State.FOLLOW_PLAYER;
                    break;
                }

                time -= Game.getDelta();

                if (time < 0) {
                    jump(MathUtils.random(360f), MathUtils.random(12f, 24f), MathUtils.random(1f, 2f));

                    velZ = 90;
                }

                break;
            case FOLLOW_PLAYER:
                time -= Game.getDelta();

                if (time < 0) {
                    Vector2 v = new Vector2(map.player.x, map.player.y).sub(x, y);
                    jump(v.angle(), v.len() * 2.5f, MathUtils.random(.5f, 1f));
                }

                break;
            case JUMP_BEGIN:
                if (jumpTime <= 0) {
                    jumpTime = .1f;
                }

                jumpTime -= Game.getDelta();

                if (jumpTime < 0) {
                    state = State.JUMP;
                }

                break;
            case JUMP:
                velX = MathUtils.cosDeg(jumpAngle) * jumpSpeed;
                velY = MathUtils.sinDeg(jumpAngle) * jumpSpeed;

                z += velZ * Game.getDelta();
                velZ -= Game.getDelta() * 512f;

                if (z < 0) {
                    z = 0;
                    velZ = 0;

                    jumpSpeed = 0;

                    state = State.JUMP_END;
                }

                break;
            case JUMP_END:
                if (jumpTime <= 0) {
                    jumpTime = .1f;
                }

                jumpTime -= Game.getDelta();

                if (jumpTime < 0) {
                    state = State.RANDOM_MOVEMENT;
                }

                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (z > 0) {
            batch.draw(spriteSheet.sheet[1][1], x, y);
        }

        TextureRegion image = null;

        switch (state) {
            case RANDOM_MOVEMENT:
            case FOLLOW_PLAYER:
                image = spriteSheet.sheet[0][0];
                break;
            case JUMP:
                image = spriteSheet.sheet[0][1];
                break;
            case JUMP_BEGIN:
            case JUMP_END:
                image = spriteSheet.sheet[1][0];
                break;
        }

        batch.draw(image, x, y + z);
    }

    public void jump(float jumpAngle, float jumpSpeed, float time) {
        this.jumpAngle = jumpAngle;
        this.jumpSpeed = jumpSpeed;

        velZ = 90;

        this.time = time / stats.count(Stat.Type.SPEED);

        state = State.JUMP_BEGIN;
    }

    @Override
    public void onAdded() {
        super.onAdded();

        jump(MathUtils.random(360f), MathUtils.random(24f, 32f), MathUtils.random(1f, 2f));
    }

    @Override
    public void onDeath() {
        super.onDeath();

        if (splitInto != null) {
            String[] splitInto = this.splitInto.get(MathUtils.random(this.splitInto.size - 1));

            for (String s : splitInto) {
                Entity entity = Game.ENTITIES.load(s);

                entity.x = x;
                entity.y = y;

                map.entities.addEntity(entity);
            }
        }
    }

    @Override
    public boolean isOnGround() {
        return z <= 0;
    }

    @Override
    public boolean isAttacking() {
        return state == State.JUMP_END;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return hitbox;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("spriteSheet")) {
            spriteSheet = Game.SPRITE_SHEETS.load(json.getString("spriteSheet"));
        }

        JsonValue splitInto = json.get("splitInto");
        if (splitInto != null) {
            this.splitInto = new Array<String[]>();

            for (JsonValue _json : splitInto) {
                this.splitInto.add(_json.asStringArray());
            }
        }
    }
}
